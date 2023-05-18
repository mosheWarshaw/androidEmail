package com.example.email.classes;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.email.R;
import com.example.email.activities.EmailBodyActivity;

public class TheViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textView;
    private TheAdapter adapterReference;
    public TheViewHolder(@NonNull View itemView, String rvToBindForFromAdapter, TheAdapter adapterReferencePar){
        super(itemView);
        adapterReference = adapterReferencePar;
        textView = (TextView) itemView.findViewById(R.id.textView);
        if(rvToBindForFromAdapter.equals("emails")) {
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(textView.getContext(), EmailBodyActivity.class);
        intent.putExtra("emailIndex", getAdapterPosition());
        textView.getContext().startActivity(intent);
    }
}
