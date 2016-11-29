package com.chetsgani.testfirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText et_msg;
    private TextInputLayout til_msg;
    private Button btn_send;
    private Button btn_cancel;
    private Button btn_getcontacts;

    private RecyclerView contacts_view;
    private ArrayList<Contacts> contacts_list;
    ContactAdapter contactAdapter;

    DatabaseReference myRef;
    static ProgressDialog dialog = null;
    InputMethodManager inputMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myRef = FirebaseDatabase.getInstance().getReference();

        til_msg = (TextInputLayout) findViewById(R.id.til_message);
        et_msg = (TextInputEditText) findViewById(R.id.et_message);

        btn_send = (Button) findViewById(R.id.send_btn);
        btn_send.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(this);
        btn_getcontacts = (Button) findViewById(R.id.get_contacts_btn);
        btn_getcontacts.setOnClickListener(this);

        contacts_view = (RecyclerView) findViewById(R.id.contacts_view);
        contacts_list = new ArrayList<>();
        contactAdapter = new ContactAdapter(contacts_list, this);

        contacts_view.setHasFixedSize(true);
        contacts_view.setLayoutManager(new LinearLayoutManager(this));
        contacts_view.setAdapter(contactAdapter);

        inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_contacts_btn:
                inputMgr.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
                dialog = ProgressDialog.show(AdminActivity.this, "", "Fetching Contacts", true);
                myRef.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("debug", "Children Count: "+dataSnapshot.getChildrenCount());
                        if (dataSnapshot.getChildrenCount() > 0) {
                            contacts_list.clear();
                            dialog.dismiss();
                            btn_getcontacts.setVisibility(View.GONE);
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                String username = postSnapshot.getKey();
                                Log.d("debug", "user: "+username);
                                Contacts contacts = new Contacts(username);
                                contacts_list.add(contacts);
                                contactAdapter.notifyDataSetChanged();
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AdminActivity.this, "No Contacts to show..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case R.id.send_btn:
                inputMgr.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
                if (!et_msg.getText().toString().equals("")) {
                    if (contacts_list.size() > 0) {
                        String msg = et_msg.getText().toString();
                        final User user = new User(msg);
                        final ArrayList<Contacts> confirmedarray = new ArrayList<>();
                        ArrayList<Contacts> arrayList = contactAdapter.getContacts();
                        for (int i = 0; i < arrayList.size(); i++) {
                            Contacts contacts = arrayList.get(i);
                            if (contacts.isSelected() == true) {
                                String SelectedContact = contacts.getUsername().toString();
                                Contacts confirmed = new Contacts();
                                confirmed.setConfirmedUser(SelectedContact);
                                confirmedarray.add(confirmed);
                            }
                        }
                        Handler handler = new Handler();
                        for (int i = 0; i < confirmedarray.size(); i++) {
                            final int finalI = i;
                            Log.d("debug", "i: "+finalI);
                            Contacts con = confirmedarray.get(finalI);
                            myRef.child("users").child(con.getConfirmedUser().toString()).setValue(user);
                            if (confirmedarray.size()-1 == finalI) {
                                Log.d("debug", "Message sent to all selected users");
                                Toast.makeText(AdminActivity.this, "Message sent to all selected users",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
                    } else {
                        Toast.makeText(AdminActivity.this, "Please select any user to send notification..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Please enter Message", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel_btn:
                finish();
                break;
        }
    }
}
