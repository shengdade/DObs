package com.example.dobs.Fragments;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dobs.Activities.BehaviorActivity;
import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.BehaviorRecord;
import com.example.dobs.R;

public class SelectContext extends ListFragment {
    private static final String TAG = "SelectContext";
    private AsyncTask task = null;
    public static final String[] environments = {
            "noisy/loud",
            "activities",
            "music",
            "cleaners/housekeeping",
            "intrusive residents",
            "crowded",
            "visitors",
            "care-dressing",
            "bathing/shower",
            "peri-care",
            "mealtime",
            "redirection",
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Choose an environment:");
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, environments));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel(false);
        }

        MainActivity.db.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                storeEnvironment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeEnvironment() {
        int position = getListView().getCheckedItemPosition();
        if (position < 0) {
            Toast.makeText(getActivity(), "You haven't select an environment", Toast.LENGTH_SHORT).show();
        } else {
            BehaviorActivity.behaviorRecord.environment = environments[position];
            task = new AddRecordTask().execute(BehaviorActivity.behaviorRecord);
            getActivity().finish();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private class AddRecordTask extends AsyncTask<BehaviorRecord, Void, Void> {

        protected Void doInBackground(BehaviorRecord... records) {
            if (!isCancelled()) {
                if (MainActivity.db.getBehaviorRecord(BehaviorActivity.behaviorRecord.time) == null) {
                    MainActivity.db.addBehaviorRecord(records[0]);// if the behavior does not exist, add
                } else {
                    MainActivity.db.updateBehaviorRecord(records[0]);// if the behavior does exist, update
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), "Record stored", Toast.LENGTH_SHORT).show();
            task = null;
        }
    }
}