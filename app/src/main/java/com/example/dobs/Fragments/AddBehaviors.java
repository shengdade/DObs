package com.example.dobs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.Behavior;
import com.example.dobs.R;

public class AddBehaviors extends Fragment {
    private static final String TAG = "AddBehaviors";
    private EditText newBehavior1;
    private EditText newBehavior2;
    private EditText newBehavior3;
    private EditText newBehavior4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add a behavior");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.frag_add_behaviors, container, false);
        newBehavior1 = (EditText) resultView.findViewById(R.id.newBehavior1);
        newBehavior2 = (EditText) resultView.findViewById(R.id.newBehavior2);
        newBehavior3 = (EditText) resultView.findViewById(R.id.newBehavior3);
        newBehavior4 = (EditText) resultView.findViewById(R.id.newBehavior4);
        return (resultView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                addNewBehaviors();
                getFragmentManager().popBackStackImmediate();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void addNewBehaviors() {
        if (!newBehavior1.getText().toString().equals("")) {
            MainActivity.patient.totalBehaviors.add(new Behavior(newBehavior1.getText().toString()));
        }
        if (!newBehavior2.getText().toString().equals("")) {
            MainActivity.patient.totalBehaviors.add(new Behavior(newBehavior2.getText().toString()));
        }
        if (!newBehavior3.getText().toString().equals("")) {
            MainActivity.patient.totalBehaviors.add(new Behavior(newBehavior3.getText().toString()));
        }
        if (!newBehavior4.getText().toString().equals("")) {
            MainActivity.patient.totalBehaviors.add(new Behavior(newBehavior4.getText().toString()));
        }
    }
}
