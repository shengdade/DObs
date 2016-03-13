package com.example.dobs.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dobs.Activities.CollectActivity;
import com.example.dobs.Activities.ExportActivity;
import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Activities.ProfileActivity;
import com.example.dobs.Activities.ViewActivity;
import com.example.dobs.Classes.DatabaseHelper;
import com.example.dobs.R;

import java.io.File;

/**
 * Created by dade on 15/02/16.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connect to database
        MainActivity.db = new DatabaseHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.frag_main, container, false);

        Button btnManage = (Button) resultView.findViewById(R.id.btnManage);
        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        Button btnCollect = (Button) resultView.findViewById(R.id.btnCollect);
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollect();
            }
        });

        Button btnView = (Button) resultView.findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewActivity.class));
            }
        });

        Button btnExport = (Button) resultView.findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExportActivity.class));
            }
        });

        return (resultView);
    }

    private void startCollect() {
        File file = new File(getActivity().getFilesDir(), MainActivity.patientFilename);
        if (file.exists()) {
            startActivity(new Intent(getActivity(), CollectActivity.class));
        } else {
            Toast.makeText(getActivity(), "Please create a profile first", Toast.LENGTH_SHORT).show();
        }
    }
}