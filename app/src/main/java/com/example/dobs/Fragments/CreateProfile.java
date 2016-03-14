package com.example.dobs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.Patient;
import com.example.dobs.R;
import com.example.dobs.Tasks.InitializeOAuthTask;

import java.util.Random;

public class CreateProfile extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "CreateProfile";
    private EditText editID;
    private int trackingIntervals = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        MainActivity.patient = new Patient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.frag_create_profile, container, false);
        editID = (EditText) resultView.findViewById(R.id.editID);
        editID.setText(getRandomID());
        RadioGroup radioMin = (RadioGroup) resultView.findViewById(R.id.radioMinutes);
        radioMin.setOnCheckedChangeListener(this);
        return (resultView);
    }

    private String getRandomID() {
        int n = new Random().nextInt(90000000) + 10000000;// generate 8-digit number, from 10000000 to 99999999
        return String.valueOf(n);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                //If the ID is empty, then it's default to be anonymous
                if (editID.getText().toString().equals("")) {
                    MainActivity.patient.ID = "(anonymous)";
                } else {
                    MainActivity.patient.ID = editID.getText().toString();
                }
                MainActivity.patient.trackingInterval = trackingIntervals;
                new InitializeOAuthTask(getActivity()).execute();
                getFragmentManager().beginTransaction().replace(R.id.fragCreate, new ChooseBehaviors()).commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioFifteen:
                trackingIntervals = 15;
                break;
            case R.id.radioThirty:
                trackingIntervals = 30;
                break;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}