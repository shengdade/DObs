package com.example.dobs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.dobs.R;

public class LoadScreen extends Fragment {
    private static final String TAG = "LoadScreen";
    private ProgressBar progressBar;
    private Handler handler;
    private int progressStatus = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.frag_load_screen, container, false);
        progressBar = (ProgressBar) resultView.findViewById(R.id.progressBar);
        handler = new Handler();
//        LinearLayout layout = (LinearLayout) resultView.findViewById(R.id.loadScreen);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        // Sleep for 20 milliseconds
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        Log.e(this.getClass().toString(), e.getMessage());
                    }
                }
                startMainFragment();
            }
        }).start();
//            }
//        });
        return resultView;
    }


    private void startMainFragment() {
        getFragmentManager().beginTransaction().replace(R.id.fragMain, new MainFragment()).commit();
    }
}
