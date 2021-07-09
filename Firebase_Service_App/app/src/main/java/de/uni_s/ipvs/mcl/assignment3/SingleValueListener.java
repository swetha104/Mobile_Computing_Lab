package de.uni_s.ipvs.mcl.assignment3;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SingleValueListener implements ValueEventListener {

    TextView read_temp = null;
    static SingleValueListener singleValueListener = null;

    public SingleValueListener(TextView read_temp) {
        this.read_temp = read_temp;
    }

    public static SingleValueListener getInstance(TextView read_temp) {
        if(singleValueListener == null) {
            singleValueListener = new SingleValueListener(read_temp);
        }
        return singleValueListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            read_temp.setText(dataSnapshot.getKey() + " : " + dataSnapshot.getValue().toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
