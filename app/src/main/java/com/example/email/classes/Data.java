package com.example.email.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/*This is a collection of the data of the app (it's here so the data and functionality of
the app is separated in order to increase the functionalitys' reusability).
The reason for it is because objects, unlike activities, aren't affected by
configuration changes, so the data is safe here.
All the data is static so you don't need to pass an object of the Data from activity
to activity.*/
public class Data {

    public static String username;

    public static final int GROUP_SIZE = 25;



    /*Look at the comments by the addOldGroup and addNewGroup to understand the
    structure of the emails in teh arraylist.*/
    private static ArrayList<JSONObject> emails = new ArrayList<>();

    public static String getEmailInfo(int emailIndexPar, String info){
        try {
            JSONObject emailObj = emails.get(emailIndexPar);
            return emailObj.get(info).toString();
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /*What i mean by "old" and "new" emails is "old emails" are emails sent to the
    user at an earlier time than the emails the user currently has been sent from the server,
    and emails that are "new" are emails that have been sent to the user more recently
    than the emails the user has on their screen.
    The reason for needing to get old emails is that the user's inbox could have
    a lot of emails, and people usually don't look at emails that are very old, rather
    they usually only look at new or very recent emails, so the server only sends
    a small amount of the most recent emails in the users inbox so they can be
    displayed on the user's screen. If the user wants to look at one of the emails
    that are older than the ones they have on their screen, then they ask the server
    for a group of emails that are older than the ones they have, and the user can
    keep requesting old emails until they've received their entire
    inbox.
    New emails are emails that someone sent the user after the user has asked for
    their most recent emails.*/
    public static void addOldEmails(JSONArray emailGroup){
        /*The emails are added to an arraylist in reverse order. so the
        last email in the user's inbox is the first email in the arraylist,
        which means it's on top in the recycler view.
        When a group of previous emails are received then they are added to the end of the arraylist
        so they are going to be below all the more recent emails in the recycler view.*/
        try {
            for(int i = emailGroup.length() - 1; i >= 0; i--){
                emails.add(emailGroup.getJSONObject(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addNewEmails(JSONArray emailGroup){
        /*Since these emails are newer they go at the beginning of the
        arraylist
        (look at the comment above the addOldEmails method to understand the setup
        of emails in the arraylist)
        and creating a new arraylist requires less copies than inserting all the new
        emails into the current arraylist and shifting all the current emails each time.*/
        ArrayList<JSONObject> newArrayList = new ArrayList<>(emailGroup.length() + emails.size());
        try {
            for(int a = 0; a < emailGroup.length(); a++){
                    newArrayList.add(emailGroup.getJSONObject(a));
            }
        }catch(JSONException e){throw new RuntimeException(e);}
        for(int b = 0; b < emails.size(); b++){
            newArrayList.add(emails.get(b));
        }
        emails = newArrayList;
    }

    //this is for when the user is sending an email.
    public static void addASingleEmail(JSONObject newEmail){
        emails.add(0, newEmail);
    }

    public static int getEmailsSize(){
        return emails.size();
    }





    private static JSONArray contacts;

    /*the user can find out how many contacts there are so they can use the getContact
    method without going out of bounds.*/
    public static int getContactsLength(){
        return contacts.length();
    }

    public static String getContact(int index){
        try {
            return contacts.get(index).toString();
        }
        catch(JSONException e){throw new RuntimeException(e);}
    }

    public static void setContacts(JSONArray contactsPar){
        /*i'm not allowing reassigning. it's essentially final, but i have to do it
        this way instead of just declaring the variable with the "final" keyword
        because final variables need to defined upon declaration and i don't have the
        contacts when declaring.*/
        if(contacts == null){
            contacts = contactsPar;
        }
    }

    public static void addNewContact(String newContact){
        contacts.put(newContact);
    }

}
