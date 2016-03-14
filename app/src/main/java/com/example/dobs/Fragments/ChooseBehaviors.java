package com.example.dobs.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.Behavior;
import com.example.dobs.Classes.Patient;
import com.example.dobs.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ChooseBehaviors extends ListFragment {
    private static final String TAG = "ChooseBehaviors";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Choose behaviors");
        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient();
        ArrayList<String> behaviors = new ArrayList<String>();
        for (Behavior behavior : MainActivity.patient.totalBehaviors) {
            behaviors.add(behavior.name);
        }
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, behaviors));
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < MainActivity.patient.totalBehaviors.size(); i++) {
            if (MainActivity.patient.trackingBehaviors.contains(MainActivity.patient.totalBehaviors.get(i))) {
                getListView().setItemChecked(i, true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                addTrackingBehaviors();
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    //If this fragment is loaded from BehaviorActivity,then stack count is 1, so pop back
                    getFragmentManager().popBackStackImmediate();
                } else {
                    //If this fragment is loaded from ProfileActivity,then stack count is 0, so finish
                    getActivity().finish();
                    Toast.makeText(getActivity(), "profile created successfully", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_add:
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    //If this fragment is loaded from BehaviorActivity,then stack count is 1, so replace fragBehavior
                    getFragmentManager().beginTransaction().replace(R.id.fragBehavior, new AddBehaviors()).addToBackStack(null).commit();
                } else {
                    //If this fragment is loaded from ProfileActivity,then stack count is 0, so replace fragCreate
                    getFragmentManager().beginTransaction().replace(R.id.fragCreate, new AddBehaviors()).addToBackStack(null).commit();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTrackingBehaviors() {
        int len = getListView().getCount();
        MainActivity.patient.trackingBehaviors.clear();
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        for (int i = 0; i < len; i++)
            if (checked.get(i) && !(MainActivity.patient.trackingBehaviors.contains(MainActivity.patient.totalBehaviors.get(i)))) {
                MainActivity.patient.trackingBehaviors.add(MainActivity.patient.totalBehaviors.get(i));
            }
        storePatient();
    }

    private void storePatient() {
        try {
            String filename = MainActivity.patientFilename;
            FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(MainActivity.patient);
            os.close();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private Patient readPatient() {
        Patient patient = null;
        try {
            FileInputStream fis = getActivity().openFileInput(MainActivity.patientFilename);
            ObjectInputStream is = new ObjectInputStream(fis);
            patient = (Patient) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return patient;
    }
}