package com.example.email.classes;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SetUpRv {
    public void setUpRv(Context context, RecyclerView rv, String rvType){
        int groupSize;
        if(rvType.equals("emails")){
            groupSize = Data.getEmailsSize();
        }
        else { //emailsOrContacts.equals("contacts")
            groupSize = Data.getContactsLength();
        }
        TheAdapter adapter = new TheAdapter(groupSize);
        adapter.setRvType(rvType);
        rv.setAdapter(adapter);

        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        rv.setLayoutManager(linearLayout);
    }
}