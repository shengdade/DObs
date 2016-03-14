package com.example.dobs.Tasks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import com.temboo.Library.Fitbit.OAuth.InitializeOAuth;
import com.temboo.core.TembooSession;

public class InitializeOAuthTask extends AsyncTask<Void, Void, String> {

    private Activity context;

    public InitializeOAuthTask(Activity context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        String mClientID = "227H64";
        String mScope = "sleep activity";
        String mForwardingURL = "fitbittester://logincallback";
        String CallbackID = "";

        try {
            // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
            TembooSession session = new TembooSession("shengdade", "myFirstApp", "ArtiavNqi3yliseQZEAMX2QLTOGanVqF");
            Log.i(this.getClass().toString(), "session created");
            InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);
            Log.i(this.getClass().toString(), "initializeOAuthChoreo created");
            // Get an InputSet object for the choreo
            InitializeOAuth.InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();
            Log.i(this.getClass().toString(), "initializeOAuthInputs created");
            // Set inputs
            initializeOAuthInputs.set_Scope(mScope);
            initializeOAuthInputs.set_ClientID(mClientID);
            initializeOAuthInputs.set_ForwardingURL(mForwardingURL);
            Log.i(this.getClass().toString(), "initializeOAuthInputs set ready");
            // Execute Choreo
            InitializeOAuth.InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);
            Log.i(this.getClass().toString(), "initializeOAuthResults created");
            //-----------------------------------------------------------------------------------------------------------------------
            String AuthorizationURL = initializeOAuthResults.get_AuthorizationURL();
            Log.i(this.getClass().toString(), "URL created: " + AuthorizationURL);
            CallbackID = initializeOAuthResults.get_CallbackID();
            Log.i(this.getClass().toString(), "CallbackID created: " + CallbackID);
            //-----------------------------------------------------------------------------------------------------------------------
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setShowTitle(true);
            intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            customTabsIntent.intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            customTabsIntent.launchUrl(context, Uri.parse(AuthorizationURL));
        } catch (Exception e) {
            // if an exception occurred, log it
            Log.i(this.getClass().toString(), e.getMessage());
        }
        return CallbackID;
    }

    protected void onPostExecute(String CallbackID) {
        try {
            new FinalizeOAuthTask(CallbackID).execute();
        } catch (Exception e) {
            // if an exception occurred, show an error message
            Log.i(this.getClass().toString(), e.getMessage());
        }
    }
}
