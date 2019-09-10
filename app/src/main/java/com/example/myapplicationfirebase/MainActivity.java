package com.example.myapplicationfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText telNo=(EditText)findViewById(R.id.editText1);
        final EditText message=(EditText)findViewById(R.id.editText2);
        Button sendMessageButton=(Button)findViewById(R.id.button1);
        telNo.setText("05469473787");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(value == null){
                        value = postSnapshot.getValue(String.class);
                    }else{
                        value =  value + " " + postSnapshot.getValue(String.class);
                    }
                }

                if(value != null){
                    message.setText(value);
                    android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();
                    sms.sendTextMessage(telNo.getText().toString(), null, message.getText().toString(), null, null);
                    dataSnapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Veritabanına erişilemedi!", Toast.LENGTH_LONG).show();
            }


        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();
                sms.sendTextMessage(telNo.getText().toString(), null, message.getText().toString(), null, null);

            }
        });
    }
}
