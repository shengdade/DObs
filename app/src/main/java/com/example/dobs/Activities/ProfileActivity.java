package com.example.dobs.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.dobs.Fragments.ChooseBehaviors;
import com.example.dobs.R;
import com.example.dobs.Fragments.CreateProfile;

import java.io.File;

/**
 * Created by dade on 15/02/16.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            File file = new File(getFilesDir(), MainActivity.patientFilename);
            if (file.exists()) {
                getFragmentManager().beginTransaction().add(R.id.fragCreate, new ChooseBehaviors()).commit();
            } else {
                getFragmentManager().beginTransaction().add(R.id.fragCreate, new CreateProfile()).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_done, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
