package com.chetsgani.testfirebase;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_contacts_btn:
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
                            Toast.makeText(AdminActivity.this, "No Contacts to show..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case R.id.send_btn:
                ArrayList<Contacts> confirmedarray = new ArrayList<>();
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
                String selected = "";
                for (int i = 0; i < confirmedarray.size(); i++) {
                    Contacts con = confirmedarray.get(i);

                    selected += con.getConfirmedUser().toString() + "\n";
                }
                Toast.makeText(AdminActivity.this, selected , Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
