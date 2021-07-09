package de.uni_s.ipvs.mcl.assignment3;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SubscribeListener implements View.OnClickListener {

    DatabaseReference mRef = null;
    EditText subscribe_location = null;
    SimpleDateFormat dateFormat = null;
    static SubscribeListener subscribeListener = null;
    TextView subscribe_temp = null;

    public SubscribeListener(DatabaseReference mRef, EditText subscribe_location, SimpleDateFormat dateFormat, TextView subscribe_temp) {
        this.mRef = mRef;
        this.subscribe_location = subscribe_location;
        this.dateFormat = dateFormat;
        this.subscribe_temp = subscribe_temp;
    }

    public static SubscribeListener getInstance(DatabaseReference mRef, EditText subscribe_location, SimpleDateFormat dateFormat, TextView subscribe_temp) {
        if(subscribeListener == null) {
            subscribeListener = new SubscribeListener(mRef, subscribe_location, dateFormat, subscribe_temp);
        }
        return subscribeListener;
    }

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        Date resultdate = new Date(currentTimeMillis);
        mRef.child("teams").child("20").child("location").child(subscribe_location.getText().toString()).child(dateFormat.format(resultdate)).addValueEventListener(ValueListener.getInstance(subscribe_temp));
    }
}
