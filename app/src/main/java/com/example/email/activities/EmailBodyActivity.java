package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.email.classes.Data;
import com.example.email.databinding.ActivityEmailBodyBinding;

public class EmailBodyActivity extends AppCompatActivity {
    private ActivityEmailBodyBinding emailBodyBinding;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        emailBodyBinding = ActivityEmailBodyBinding.inflate(getLayoutInflater());
        setContentView(emailBodyBinding.getRoot());
        setSupportActionBar(emailBodyBinding.emailBodysToolbar.toolbar);
        int ebaEmailIndex = getIntent().getIntExtra("emailIndex", -1);
        String emailBody = Data.getEmailInfo(ebaEmailIndex, "body");
        emailBodyBinding.textView.setText(emailBody);
    }
}