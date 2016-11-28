package com.chetsgani.testfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chetan Gani on 11/28/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    ArrayList<Contacts> arrayList = new ArrayList<Contacts>();
    Context context;

    public ContactAdapter(ArrayList<Contacts> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Contacts contacts = arrayList.get(position);
        holder.tv_contact.setText(contacts.getUsername());
        holder.contact_box.setChecked(contacts.isSelected());
        holder.contact_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contacts.setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_contact;
        CheckBox contact_box;

        public ContactViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_contact = (TextView) itemView.findViewById(R.id.contacts_txt);
            contact_box = (CheckBox) itemView.findViewById(R.id.contacts_checkbox);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public ArrayList<Contacts> getContacts() {
        return arrayList;
    }
}
