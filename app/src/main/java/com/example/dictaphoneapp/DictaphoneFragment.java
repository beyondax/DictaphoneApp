package com.example.dictaphoneapp;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;


public class DictaphoneFragment extends Fragment {

    private Button mStartRecordingButton;
    private Button mStopRecordingButton;


    static ArrayList<File> recordedAudioFiles = new ArrayList<>();
    private ListView mListView;


    private static final String TAG = "DictaphoneFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictaphone, container, false);

        mStartRecordingButton = view.findViewById(R.id.start_recording_button);
        mStopRecordingButton = view.findViewById(R.id.stop_recording_button);
        mListView = view.findViewById(R.id.files_list_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, recordedAudioFiles);

        mListView.setAdapter(adapter);


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
        Intent intent = new Intent(getActivity(), DictaphoneService.class);
        intent.putExtra(TAG, "ForegroundService");
        final ContextWrapper contextWrapper = new ContextWrapper(getActivity());
        contextWrapper.startService(intent);


    }

    private void stopService() {
        Intent intent = new Intent(getActivity(), DictaphoneService.class);
        intent.setAction(DictaphoneService.ACTION_CLOSE);
        final ContextWrapper contextWrapper = new ContextWrapper(getActivity());
        contextWrapper.startService(intent);
    }


}
