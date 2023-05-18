package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.email.classes.Data;
import com.example.email.classes.ServerConnector;
import com.example.email.databinding.ActivitySendEmailBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class SendEmailActivity extends AppCompatActivity {
    private String urlStrForSe = "http://10.0.0.5:5000/sendEmailFromAndroid";
    private ActivitySendEmailBinding seBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seBinding = ActivitySendEmailBinding.inflate(getLayoutInflater());
        setContentView(seBinding.getRoot());
        setSupportActionBar(seBinding.sendEmailsToolbar.toolbar);

        seBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject seJson = new JSONObject();
                try{
                    seJson.put("sender", Data.username);
                    seJson.put("receiver", seBinding.receiverEditText.getText().toString());
                    seJson.put("subject", seBinding.subjectEditText.getText().toString());
                    seJson.put("body", seBinding.bodyEditText.getText().toString());
                }catch(JSONException e){throw new RuntimeException(e);}
                ServerConnector seServerConnector = new ServerConnector();
                seServerConnector.connect(SendEmailActivity.this, urlStrForSe, "sendEmailFromAndroid", seJson, null);
            }
        });
    }
}