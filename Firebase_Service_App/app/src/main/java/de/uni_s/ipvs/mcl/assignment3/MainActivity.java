package de.uni_s.ipvs.mcl.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String city = "munich,de", selection;
    //    String city, selection;
    String API = "5d1d5d54c7c42296707ffddd5263d1ed";
    TextView tempTxt;
    String[] cities = {"munich", "Ulm", "Heidelberg"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cities_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
        final EditText write_location= findViewById(R.id.write_location);
        final EditText write_temp =findViewById(R.id.write_temp);

        Button write_button = findViewById(R.id.write_button);

        final EditText read_location = findViewById(R.id.read_location);
        final TextView read_temp = findViewById(R.id.read_temp);

        Button read_button = findViewById(R.id.read_button);

        final EditText subscribe_location = findViewById(R.id.subscribe_location);
        final TextView subscribe_temp = findViewById(R.id.subscribe_temp);

        Button subscribe_button = findViewById(R.id.subscribe_button);

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        write_button.setOnClickListener(WriteListener.getInstance(mRef, write_location, dateFormat, timeFormat, write_temp));

        read_button.setOnClickListener(ReadListener.getInstance(mRef, read_location, dateFormat, read_temp));

        subscribe_button.setOnClickListener(SubscribeListener.getInstance(mRef, subscribe_location, dateFormat, subscribe_temp));

        tempTxt = (TextView) findViewById(R.id.textView);

        new weatherTask().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection = cities[position];
        if (selection.equals("Stuttgart")){
            city = "stuttgart,de";
        }
        else if (selection.equals("Ulm")){
            city = "ulm,de";
        }
        else if (selection.equals("Heidelberg")){
            city = "Heidelberg";
        }
        new weatherTask().execute();
        Toast.makeText(this, cities[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selection = (String) parent.getSelectedItem();
        if (selection.equals("Stuttgart")){
            city = "stuttgart,de";
        }
        else if (selection.equals("Ulm")){
            city = "ulm,de";
        }
        else if (selection.equals("Heidelberg")){
            city = "Heidelberg";
        }
        Toast.makeText(this, (CharSequence) parent.getSelectedItem(), Toast.LENGTH_LONG).show();

    }



    class weatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String doInBackground(String... args){
            String response = HttpRequest.executeGet("https://api.openweathermap.org/data/2.5/weather?q=" +
                    city + "&units=metric&appid=" + API);

//            String response = HttpRequest.executeGet("https://api.openweathermap.org/data/2.5/weather?q=stuttgart,de&units=metric&appid=5d1d5d54c7c42296707ffddd5263d1ed");
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            if(result  != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                    Long updatedAt = jsonObj.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    String temp = main.getString("temp") + "°C";
                    tempTxt.setText(temp);

                } catch (JSONException e) {
//                findViewById(R.id.loader).setVisibility(View.GONE);
                    e.printStackTrace();
                    findViewById(R.id.textView).setVisibility(View.VISIBLE);
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "result is null!!", Toast.LENGTH_LONG).show();
            }
        }
    }

}

