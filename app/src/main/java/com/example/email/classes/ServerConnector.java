package com.example.email.classes;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.email.activities.ContactsActivity;
import com.example.email.activities.HomeActivity;
import com.example.email.activities.SendEmailActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnector {

    public void connect(AppCompatActivity activityReferencePar, String urlStrPar, String endPointPar, JSONObject jsonToSendPar, String emailsToGetPar) {
        ConnectorThread t = new ConnectorThread();
        t.activityReference = activityReferencePar;
        t.urlStr = urlStrPar;
        t.endPoint = endPointPar;
        t.jsonToSend = jsonToSendPar;
        t.emailsToGet = emailsToGetPar;
        t.start();
    }

    private class ConnectorThread extends Thread {
        private AppCompatActivity activityReference;
        public String urlStr;
        public String endPoint;
        public JSONObject jsonToSend;
        public String emailsToGet;

        @Override
        public void run() {
            URL url = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            DataOutputStream writer = null;
            String username;
            Exception exception = null;
            try {
                url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                writer = new DataOutputStream(urlConnection.getOutputStream());
                writer.write(jsonToSend.toString().getBytes());
                writer.flush();
                writer.close();
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String input = reader.readLine();
                handleReturnedData(input, jsonToSend);
            }
            /*If any error occurs then I will catch it, close all the resources,
            and throw the error (errors shouldn't pass silently).*/
            catch (Exception e) {
                exception = e;
            }
            /*Closing the resources could cause errors, and i don't put them all in
            one try-catch because i want the rest to be able to close even if one
            threw an error while being closed.*/
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                /*I only store the first error. The first error is the bug. The rest
                could just eb errors happening because of the first error, so throwing
                the last error wouldn't be presenting the source of the problem.*/
                if (exception == null) {
                    exception = e;
                }
            }
            try {
                reader.close();
            } catch (Exception e) {
                if (exception == null) {
                    exception = e;
                }
            }
            try {
                writer.close();
            } catch (Exception e) {
                if (exception == null) {
                    exception = e;
                }
            }
            if(exception != null){
                throw new RuntimeException(exception);
            }
        }
        

        public void handleReturnedData(String returnedData, JSONObject sentJson) throws JSONException {
            JSONObject returnedJson = new JSONObject(returnedData);
            if ((boolean) returnedJson.get("hasMessage")) {
                String message = (String) returnedJson.get("message");
                handleMessage(message);
            }
            else {
                onNoMessage(returnedJson, sentJson);
            }
        }


        public void onNoMessage(JSONObject returnedJson, JSONObject sentJson) throws JSONException {
            if (endPoint.equals("createAccount") || endPoint.equals("login")) {
                Data.username = sentJson.get("username").toString();
                Data.password = sentJson.get("password").toString();
                if(returnedJson.getJSONArray("emails").length() != 0){
                    JSONArray emailGroup = returnedJson.getJSONArray("emails");
                    Data.addOldEmails(emailGroup);
                }
                Data.setContacts(returnedJson.getJSONArray("contacts"));
                Intent intent = new Intent((Context) activityReference, HomeActivity.class);
                ((Context) activityReference).startActivity(intent);
            }
            else if(endPoint.equals("getEmails")){
                JSONArray emailGroup = returnedJson.getJSONArray("emails");
                if(emailsToGet.equals("new")){
                    Data.addNewEmails(emailGroup);
                }
                else{ //emailsToGet.equals("old")
                    Data.addOldEmails(emailGroup);
                }
                ((HomeActivity)activityReference).refreshAdapter(Data.getEmailsSize());
            }
            else if(endPoint.equals("addContact")){
                Data.addNewContact(sentJson.getString("contact"));
                ((ContactsActivity)activityReference).refreshAdapter(Data.getContactsLength());
            }
            else if(endPoint.equals("sendEmailFromAndroid")){
                JSONObject sentEmail = sentJson;
                sentEmail.put("dt_tm", returnedJson.getString("dt_tm"));
                sentEmail.put("emailNum", returnedJson.getString("emailNum"));
                Data.addASingleEmail(sentEmail);
                ((SendEmailActivity)activityReference).finish();
                /*The onResume method will reconstruct the adapter to include the
                email just sent.*/
            }
        }


        public void handleMessage(String message) {
            /*Making a toast can't be done from the connector
            thread. it has to be done from the ui thread.*/
            activityReference.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activityReference, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
