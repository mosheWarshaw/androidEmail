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
    private ActivityHomeBinding haBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        haBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(haBinding.getRoot());
        setSupportActionBar(haBinding.homeToolbar.toolbar);


        SetUpRv haSetUpRv = new SetUpRv();
        haSetUpRv.setUpRv(getApplicationContext(), haBinding.emailRv, "emails");
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
        if(Data.getEmailsSize() > haBinding.emailRv.getAdapter().getItemCount()){
            ((TheAdapter)haBinding.emailRv.getAdapter()).reassignItemCount(Data.getEmailsSize());
            haBinding.emailRv.getAdapter().notifyItemRangeChanged(0, Data.getEmailsSize() );
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
        ServerConnector haConnector = new ServerConnector();
        try {
            JSONObject jsonObjForPrevGroup = new JSONObject();
            jsonObjForPrevGroup.put("username", Data.username);
            jsonObjForPrevGroup.put("startEmailNum", startEmailNumPar);
            jsonObjForPrevGroup.put("hasEndEmailNum", hasEndEmailNumPar);
            if(hasEndEmailNumPar){
                jsonObjForPrevGroup.put("endEmailNum", endEmailNumPar);
            }
            haConnector.connect(this, "http://10.0.0.5:5000/getEmails", "getEmails", jsonObjForPrevGroup, emailsToGet);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshAdapter(int newItemCount){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TheAdapter)haBinding.emailRv.getAdapter()).reassignItemCount(newItemCount);
                haBinding.emailRv.getAdapter().notifyItemRangeChanged(0, newItemCount);
            }
        });
    }
}