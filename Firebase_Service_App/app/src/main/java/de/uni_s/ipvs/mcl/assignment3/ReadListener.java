package de.uni_s.ipvs.mcl.assignment3;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadListener implements View.OnClickListener {

    DatabaseReference mRef = null;
    EditText read_location = null;
    TextView read_temp = null;
    SimpleDateFormat dateFormat = null;
    static ReadListener readListener = null;


    public ReadListener(DatabaseReference mRef, EditText read_location, SimpleDateFormat dateFormat, TextView read_temp) {
        this.mRef = mRef;
        this.read_location = read_location;
        this.dateFormat = dateFormat;
        this.read_temp = read_temp;
    }

    public static ReadListener getInstance(DatabaseReference mRef, EditText read_location, SimpleDateFormat dateFormat, TextView read_temp) {
        if(readListener == null) {
            readListener = new ReadListener(mRef, read_location, dateFormat, read_temp);
        }
        return readListener;
    }

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        Date resultdate = new Date(currentTimeMillis);
        mRef.child("teams").child("20").child("location").child(read_location.getText().toString()).child(dateFormat.format(resultdate)).addListenerForSingleValueEvent(SingleValueListener.getInstance(read_temp));
    }
}
