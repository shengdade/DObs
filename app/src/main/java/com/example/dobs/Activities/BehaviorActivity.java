package com.example.dobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.dobs.Classes.BehaviorRecord;
import com.example.dobs.Fragments.SelectBehavior;
import com.example.dobs.R;

/**
 * Created by dade on 15/02/16.
 */
public class BehaviorActivity extends AppCompatActivity {
    private static final String TAG = "BehaviorActivity";
    public static BehaviorRecord behaviorRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        behaviorRecord = new BehaviorRecord();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.fragBehavior, new SelectBehavior()).commit();
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
