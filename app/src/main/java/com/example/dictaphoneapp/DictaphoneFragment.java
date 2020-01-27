package com.example.dictaphoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class DictaphoneFragment extends Fragment {

    private Button mStartRecordingButton;
    private Button mStopRecordingButton;
    private Button mPlayButton;
    private Button mPauseButton;

    private static final String TAG = "DictaphoneFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_dictaphone, container, false);
       mStartRecordingButton = view.findViewById(R.id.start_recording_button);
       mStopRecordingButton = view.findViewById(R.id.stop_recording_button);
       mPlayButton = view.findViewById(R.id.start_playing_button);
       mPauseButton = view.findViewById(R.id.stop_playing_button);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }

        });

        mStopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void startService() {
        Intent serviceIntent = new Intent(getActivity(), DictaphoneService.class);
        serviceIntent.putExtra(TAG, "ForegroundService");
        startService();


    }

    private void stopService() {
        Intent serviceIntent = new Intent(getActivity(), DictaphoneService.class);
        serviceIntent.setAction(DictaphoneService.ACTION_CLOSE);
        startService();
    }


}
