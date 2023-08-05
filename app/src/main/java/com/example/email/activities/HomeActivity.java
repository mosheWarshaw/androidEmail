package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.email.R;
import com.example.email.classes.Data;
import com.example.email.classes.ServerConnector;
import com.example.email.classes.SetUpRv;
import com.example.email.classes.TheAdapter;
import com.example.email.databinding.ActivityHomeBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding viewBinding;
    private String getEmailsUrlStr = "http://10.0.0.5:5000/getEmails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        setSupportActionBar(viewBinding.homeToolbar.toolbar);


        SetUpRv setUpRv = new SetUpRv();
        setUpRv.setUpRv(getApplicationContext(), viewBinding.emailRv, "emails");
    }

    /*When a user leaves this activity to send an email, and then they come
    back to this activity once they've sent it, they're going to want to see the
    email they just sent be in the email recycler view, so this method checks
    that when this activity resumes if there are more emails than the recycler view
    has, then an email was sent and added to the arraylist of emails and it now needs
    to be included in the recycler view.*/
    @Override
    protected void onResume() {
        super.onResume();
        if(Data.getEmailsSize() > viewBinding.emailRv.getAdapter().getItemCount()){
            ((TheAdapter)viewBinding.emailRv.getAdapter()).reassignItemCount(Data.getEmailsSize());
            viewBinding.emailRv.getAdapter().notifyItemRangeChanged(0, Data.getEmailsSize() );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.contacts) {
            intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.sendEmail) {
            intent = new Intent(this, SendEmailActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.getOldGroup){
            int emailNumOfOldestEmail = Integer.parseInt(Data.getEmailInfo( Data.getEmailsSize() - 1, "emailNum"));
            getEmailGroup(true, emailNumOfOldestEmail - Data.GROUP_SIZE, emailNumOfOldestEmail - 1, "old");
        }
        /*It's called "refreshInbox" even thought the user only has a small number
        of the emails from their inbox, but to the usr they jut need to click a button
        to see new or old emails, so it's as if the entire inbox is there because
        it's all available at the click of a button.*/
        else if(item.getItemId() == R.id.refreshInbox){
            int emailNumOfMostRecentEmail = Integer.parseInt(Data.getEmailInfo( 0, "emailNum"));
            getEmailGroup(false, emailNumOfMostRecentEmail, null, "new");
        }
        return true;
    }

    public void getEmailGroup(boolean hasEndEmailNumPar, int startEmailNumPar, Integer endEmailNumPar, String emailsToGet){
        ServerConnector connector = new ServerConnector();
        try {
            JSONObject jsonToSend = new JSONObject();
            jsonToSend.put("type", "android");
            jsonToSend.put("username", Data.username);
            jsonToSend.put("password", Data.password);
            jsonToSend.put("startEmailNum", startEmailNumPar);
            jsonToSend.put("hasEndEmailNum", hasEndEmailNumPar);
            if(hasEndEmailNumPar){
                jsonToSend.put("endEmailNum", endEmailNumPar);
            }
            connector.connect(this, getEmailsUrlStr, "getEmails", jsonToSend, emailsToGet);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshAdapter(int newItemCount){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TheAdapter)viewBinding.emailRv.getAdapter()).reassignItemCount(newItemCount);
                viewBinding.emailRv.getAdapter().notifyItemRangeChanged(0, newItemCount);
            }
        });
    }
}