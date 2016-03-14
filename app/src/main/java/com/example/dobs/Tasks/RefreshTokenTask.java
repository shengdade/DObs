package com.example.dobs.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dobs.Activities.MainActivity;
import com.temboo.Library.Fitbit.OAuth.RefreshToken;
import com.temboo.Library.Fitbit.OAuth.RefreshToken.RefreshTokenInputSet;
import com.temboo.Library.Fitbit.OAuth.RefreshToken.RefreshTokenResultSet;
import com.temboo.core.TembooSession;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class RefreshTokenTask extends AsyncTask<Void, Void, Void> {

    private AppCompatActivity context;

    public RefreshTokenTask(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        String mClientID = "227H64";
        String mClientSecret = "c0a8fbb09fa1fbee8dd28234a53c1f9d";

        try {
            // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
            TembooSession session = new TembooSession("shengdade", "myFirstApp", "ArtiavNqi3yliseQZEAMX2QLTOGanVqF");
            //-----------------------------------------------------------------------------------------------------------------------
            RefreshToken refreshTokenChoreo = new RefreshToken(session);
            Log.i(this.getClass().toString(), "refreshTokenChoreo created: ");
            // Get an InputSet object for the choreo
            RefreshTokenInputSet refreshTokenInputs = refreshTokenChoreo.newInputSet();
            // Set inputs
            refreshTokenInputs.set_RefreshToken(MainActivity.patient.refreshToken);
            refreshTokenInputs.set_ClientSecret(mClientSecret);
            refreshTokenInputs.set_ClientID(mClientID);
            Log.i(this.getClass().toString(), "refreshTokenInputs created: ");
            // Execute Choreo
            RefreshTokenResultSet refreshTokenResults = refreshTokenChoreo.execute(refreshTokenInputs);
            Log.i(this.getClass().toString(), "refreshTokenResults created: ");
            MainActivity.patient.accessToken = refreshTokenResults.get_AccessToken();
            Log.i(this.getClass().toString(), "New token created: " + MainActivity.patient.accessToken);
            MainActivity.patient.refreshToken = refreshTokenResults.get_NewRefreshToken();
            Log.i(this.getClass().toString(), "New refreshToken created: " + MainActivity.patient.refreshToken);
        } catch (Exception e) {
            // if an exception occurred, log it
            Log.i(this.getClass().toString(), e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(Void CallbackID) {
        try {
            storePatient();
        } catch (Exception e) {
            // if an exception occurred, show an error message
            Log.i(this.getClass().toString(), e.getMessage());
        }
    }

    private void storePatient() {
        try {
            String filename = MainActivity.patientFilename;
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(MainActivity.patient);
            os.close();
            fos.close();
        } catch (Exception e) {
            Log.i(this.getClass().toString(), e.getMessage());
        }
    }
}
