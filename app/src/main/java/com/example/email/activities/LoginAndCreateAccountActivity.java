package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.email.classes.ServerConnector;
import com.example.email.databinding.ActivityLoginAndCreateAccountBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginAndCreateAccountActivity extends AppCompatActivity {
    private ActivityLoginAndCreateAccountBinding lcaBinding;
    private String urlStrForLogin = "http://10.0.0.5:5000/login";
    private String urlStrForCreateAccount = "http://10.0.0.5:5000/createAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lcaBinding = ActivityLoginAndCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(lcaBinding.getRoot());
        setSupportActionBar(lcaBinding.loginAndCreateAccountToolbar.toolbar);

        lcaBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject loginJson = gatherDataToSend();
                ServerConnector loginConnector = new ServerConnector();
                loginConnector.connect(LoginAndCreateAccountActivity.this, urlStrForLogin, "login", loginJson, null);
            }
        });

        lcaBinding.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject createAccountJson = gatherDataToSend();
                ServerConnector createAccountConnector = new ServerConnector();
                createAccountConnector.connect(LoginAndCreateAccountActivity.this, urlStrForCreateAccount, "createAccount", createAccountJson, null);
            }
        });
    }

    public JSONObject gatherDataToSend(){
        JSONObject json = new JSONObject();
        try {
            json.put("username", lcaBinding.username.getText().toString());
            json.put("password", lcaBinding.password.getText().toString());
            json.put("type", "android");
        }catch(JSONException e){throw new RuntimeException(e);}
        return json;
    }
}