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
    private ActivityContactsBinding viewBinding;
    private String addContactUrlStr = "http://10.0.0.5:5000/addContact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        setSupportActionBar(viewBinding.contactsToolbar.toolbar);
        SetUpRv caSetUpRv = new SetUpRv();
        caSetUpRv.setUpRv(getApplicationContext(), viewBinding.contactsRv, "contacts");

        viewBinding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonToSend = new JSONObject();
                try {
                    jsonToSend.put("type", "android");
                    jsonToSend.put("username", Data.username);
                    jsonToSend.put("password", Data.password);
                    jsonToSend.put("contact", viewBinding.contactEditText.getText().toString());
                }catch(JSONException e){throw new RuntimeException(e);}
                ServerConnector connector = new ServerConnector();
                connector.connect(ContactsActivity.this, addContactUrlStr, "addContact", jsonToSend, null);
            }
        });
    }

    public void refreshAdapter(int newItemCount){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TheAdapter)viewBinding.contactsRv.getAdapter()).reassignItemCount(newItemCount);
                viewBinding.contactsRv.getAdapter().notifyItemRangeChanged(0, newItemCount);
            }
        });
    }
}