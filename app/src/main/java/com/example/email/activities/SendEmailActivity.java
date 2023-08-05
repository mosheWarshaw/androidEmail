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
    private String sendEmailUrlStr = "http://10.0.0.5:5000/sendEmailFromAndroid";
    private ActivitySendEmailBinding viewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySendEmailBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        setSupportActionBar(viewBinding.sendEmailsToolbar.toolbar);

        viewBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonToSend = new JSONObject();
                try{
                    jsonToSend.put("password", Data.password);
                    jsonToSend.put("sender", Data.username);
                    jsonToSend.put("receiver", viewBinding.receiverEditText.getText().toString());
                    jsonToSend.put("subject", viewBinding.subjectEditText.getText().toString());
                    jsonToSend.put("body", viewBinding.bodyEditText.getText().toString());
                }catch(JSONException e){throw new RuntimeException(e);}
                ServerConnector connector = new ServerConnector();
                connector.connect(SendEmailActivity.this, sendEmailUrlStr, "sendEmailFromAndroid", jsonToSend, null);
            }
        });
    }
}