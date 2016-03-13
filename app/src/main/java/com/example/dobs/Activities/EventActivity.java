package com.example.dobs.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dobs.Classes.EventRecord;
import com.example.dobs.R;

/**
 * Created by dade on 15/02/16.
 */
public class EventActivity extends AppCompatActivity {
    public static EventRecord eventRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventRecord = new EventRecord();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
//                Toast.makeText(this, "This button would store events later", Toast.LENGTH_SHORT).show();
                finish();
                Toast.makeText(this, "Record stored", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
