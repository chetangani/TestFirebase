package com.chetsgani.testfirebase;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button submit_btn, stop_btn;
    TextInputEditText et_user, et_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        submit_btn = (Button) findViewById(R.id.submit_btn);
        stop_btn = (Button) findViewById(R.id.stop_service_btn);

        et_user = (TextInputEditText) findViewById(R.id.et_username);
        et_add = (TextInputEditText) findViewById(R.id.et_useraddress);

        startService(new Intent(MainActivity.this, NotifyListener.class));

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_add.getText().toString().equals("")) {
                    String msg = et_add.getText().toString();

                    User user = new User(msg);

                    myRef.child("users").child("Chetan G").setValue(user);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter msg in address field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("users").child("Chetan G").removeValue();
                stopService(new Intent(MainActivity.this, NotifyListener.class));
            }
        });
    }
}
