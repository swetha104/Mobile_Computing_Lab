package de.uni_s.ipvs.mcl.assignment3;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteListener implements View.OnClickListener {

    DatabaseReference mRef = null;
    EditText write_location = null;
    SimpleDateFormat dateFormat = null;
    SimpleDateFormat timeFormat = null;
    TextView write_temp = null;
    static WriteListener writeListener = null;

    public WriteListener(DatabaseReference mRef, EditText write_location, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat, TextView write_temp) {
        this.mRef = mRef;
        this.write_location = write_location;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.write_temp = write_temp;
    }

    public static WriteListener getInstance(DatabaseReference mRef, EditText write_location, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat, TextView write_temp) {
        if(writeListener == null) {
            writeListener = new WriteListener(mRef, write_location, dateFormat, timeFormat, write_temp);
        }
        return writeListener;
    }

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        Date resultdate = new Date(currentTimeMillis);
        mRef.child("teams").child("20").child("location").child(write_location.getText().toString()).child(dateFormat.format(resultdate)).child(timeFormat.format(resultdate)).setValue(write_temp.getText().toString());
    }
}
