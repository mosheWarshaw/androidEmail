package com.example.email.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.email.R;

public class TheAdapter extends RecyclerView.Adapter<TheViewHolder>{
    private int itemCount;
    @Override
    public int getItemCount() {
        return itemCount;
    }
    public void reassignItemCount(int newItemCount){
        itemCount = newItemCount;
    }

    private String rvType;
    public void setRvType(String rvTypePar){
        rvType = rvTypePar;
    }

    public TheAdapter(int itemCountPar){
        itemCount = itemCountPar;
    }
    @NonNull
    @Override
    public TheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if(rvType.equals("emails")){
            layout = R.layout.email_preview;
        }
        else{//rvToBindFor.equals("contacts")
            layout = R.layout.single_contact_view;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                layout, parent, false);
        return new TheViewHolder(itemView, rvType, this);
    }


    @Override
    public void onBindViewHolder(@NonNull TheViewHolder holder, int position) {
        String text = null;
        if(rvType.equals("emails")){
            String sender = Data.getEmailInfo(position, "sender");
            String subject = Data.getEmailInfo(position, "subject");
            String dt_tm = Data.getEmailInfo(position, "dt_tm");
            text = sender + "     " + subject + "     " + dt_tm;
        }
        else{ //rvToBindFor.equals("contacts")
            text = Data.getContact(position);
        }
        holder.textView.setText(text);
    }
}