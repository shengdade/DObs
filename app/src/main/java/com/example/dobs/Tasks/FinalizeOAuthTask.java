package com.example.dobs.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dobs.Activities.MainActivity;
import com.temboo.Library.Fitbit.OAuth.FinalizeOAuth;
import com.temboo.core.TembooSession;

public class FinalizeOAuthTask extends AsyncTask<Void, Void, Void> {
    private String CallbackID;

    public FinalizeOAuthTask(String CallbackID) {
        this.CallbackID = CallbackID;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        String mClientID = "227H64";
        String mClientSecret = "c0a8fbb09fa1fbee8dd28234a53c1f9d";

        try {
            // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
            TembooSession session = new TembooSession("shengdade", "myFirstApp", "z8QMpb5tMIsoERAMa7wdldFNVFi4BGKS");
            //-----------------------------------------------------------------------------------------------------------------------
            FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);
            Log.i(this.getClass().toString(), "finalizeOAuthChore created: ");
            // Get an InputSet object for the choreo
            FinalizeOAuth.FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();
            Log.i(this.getClass().toString(), "finalizeOAuthInputs created: ");
            // Set inputs
            finalizeOAuthInputs.set_ClientSecret(mClientSecret);
            finalizeOAuthInputs.set_CallbackID(this.CallbackID);
            finalizeOAuthInputs.set_ClientID(mClientID);
            Log.i(this.getClass().toString(), "finalizeOAuthInputs set ready");
            // Execute Choreo
            FinalizeOAuth.FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
            Log.i(this.getClass().toString(), "finalizeOAuthResults created");
            //-----------------------------------------------------------------------------------------------------------------------
            MainActivity.patient.accessToken = finalizeOAuthResults.get_AccessToken();
            Log.i(this.getClass().toString(), "Token created: " + MainActivity.patient.accessToken);
            MainActivity.patient.refreshToken = finalizeOAuthResults.get_RefreshToken();
            Log.i(this.getClass().toString(), "RefreshToken created: " + MainActivity.patient.refreshToken);
            //-----------------------------------------------------------------------------------------------------------------------
        } catch (Exception e) {
            // if an exception occurred, log it
            Log.e(this.getClass().toString(), e.getMessage());
        }
        return null;
    }
}
