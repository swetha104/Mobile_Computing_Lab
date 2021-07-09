package de.uni_s.ipvs.mcl.assignment3;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ValueListener implements ValueEventListener {

    TextView subscribe_temp = null;
    static ValueListener valueListener = null;

    public ValueListener(TextView subscribe_temp) {
        this.subscribe_temp = subscribe_temp;
    }

    public static ValueListener getInstance(TextView subscribe_temp) {
        if(valueListener == null) {
            valueListener = new ValueListener(subscribe_temp);
        }
        return valueListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            Iterator it = map.entrySet().iterator();
            int count = 0;
            float total = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                total = total + Float.parseFloat((String)pair.getValue());
                count++;
                it.remove();
            }
            float avgTemp = total/count;
            subscribe_temp.setText(dataSnapshot.getKey() + " : " + avgTemp);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
