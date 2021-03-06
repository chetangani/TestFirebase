package com.chetsgani.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Constant to store shared preferences
    public static final String SHARED_PREF = "notificationapp";

    //To store the firebase id in shared preferences
    public static final String USER_NAME = "username";

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button register_btn, stop_btn;
    TextInputEditText et_user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        register_btn = (Button) findViewById(R.id.register_btn);
        stop_btn = (Button) findViewById(R.id.stop_service_btn);

        et_user = (TextInputEditText) findViewById(R.id.et_username);

        //Opening shared preference
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        //Opening the shared preferences editor to save values
        editor = sharedPreferences.edit();

        /*startService(new Intent(MainActivity.this, NotifyListener.class));*/

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(et_user.getWindowToken(), 0);
                if (!et_user.getText().toString().equals("")) {
                    final String username = et_user.getText().toString();
                    String msg = "";

                    final User user = new User(msg);

                    myRef.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getKey().equals(sharedPreferences.getString(USER_NAME, ""))) {
                                Snackbar.make(v, "Device Already registered", Snackbar.LENGTH_LONG).show();
                                Log.d("debug", "Already existed");
                            } else {
                                Log.d("debug", "Created new one...");
                                editor.putString(USER_NAME, username);
                                editor.apply();
                                myRef.child("users").child(sharedPreferences.getString(USER_NAME, "")).setValue(user);
                                et_user.setText("");
                                Snackbar.make(v, "Successfully Registered", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, NotifyListener.class));
            }
        });
    }
}
