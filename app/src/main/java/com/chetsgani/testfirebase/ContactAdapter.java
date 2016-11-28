package com.chetsgani.testfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Contacts contacts = arrayList.get(position);
        holder.tv_contact.setText(contacts.getUsername());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tv_contact;

        public ContactViewHolder(View itemView) {
            super(itemView);

            tv_contact = (TextView) itemView.findViewById(R.id.contacts_txt);
        }
    }
}
