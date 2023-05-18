package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.email.classes.Data;
import com.example.email.classes.ServerConnector;
import com.example.email.classes.SetUpRv;
import com.example.email.classes.TheAdapter;
import com.example.email.databinding.ActivityContactsBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding cBinding;
    private String urlStrForContact = "http://10.0.0.5:5000/addContact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cBinding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(cBinding.getRoot());
        setSupportActionBar(cBinding.contactsToolbar.toolbar);
        SetUpRv caSetUpRv = new SetUpRv();
        caSetUpRv.setUpRv(getApplicationContext(), cBinding.contactsRv, "contacts");

        cBinding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject contactJson = new JSONObject();
                try {
                    contactJson.put("username", Data.username);
                    contactJson.put("contact", cBinding.contactEditText.getText().toString());
                }catch(JSONException e){throw new RuntimeException(e);}
                ServerConnector cConnector = new ServerConnector();
                cConnector.connect(ContactsActivity.this, urlStrForContact, "addContact", contactJson, null);
            }
        });
    }

    public void refreshAdapter(int newItemCount){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TheAdapter)cBinding.contactsRv.getAdapter()).reassignItemCount(newItemCount);
                cBinding.contactsRv.getAdapter().notifyItemRangeChanged(0, newItemCount);
            }
        });
    }
}