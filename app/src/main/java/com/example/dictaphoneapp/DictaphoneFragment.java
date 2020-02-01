package com.example.dictaphoneapp;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;


public class DictaphoneFragment extends Fragment {

    private Button mStartRecordingButton;
    private Button mStopRecordingButton;
    private RecyclerView mFilesRecyclerView;
    private RecyclerView.Adapter<FileListAdapter.FileListViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static ArrayList<File> recordedAudioFiles = new ArrayList<>();


    private static final String TAG = "DictaphoneFragment";
    private ArrayList<String> mFilePathList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictaphone, container, false);

        mStartRecordingButton = view.findViewById(R.id.start_recording_button);
        mStopRecordingButton = view.findViewById(R.id.stop_recording_button);

        mFilesRecyclerView = view.findViewById(R.id.files_recycler_view);

        mFilesRecyclerView.setHasFixedSize(true);

        mFilesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mFilePathList.add("/sdcard/sdcard");


        File outputFolder = new File("/sdcard/RecordedAudio");
        File[] files = outputFolder.listFiles();
        int i = 0;
        for (File f : files) {
            if (f.isFile()) {
                try {
                    mFilePathList.add(i, f.getName());
                    Log.d(TAG, "files: " + f.getName());
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new FileListAdapter(getActivity(), mFilePathList);

        mFilesRecyclerView.setLayoutManager(mLayoutManager);
        mFilesRecyclerView.setAdapter(mAdapter);

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
        Intent intent = new Intent(getActivity(), DictaphoneRecorderService.class);
        intent.putExtra(TAG, "ForegroundService");
        final ContextWrapper contextWrapper = new ContextWrapper(getActivity());
        contextWrapper.startService(intent);


    }

    private void stopService() {
        Intent intent = new Intent(getActivity(), DictaphoneRecorderService.class);
        intent.setAction(DictaphoneRecorderService.ACTION_CLOSE);
        final ContextWrapper contextWrapper = new ContextWrapper(getActivity());
        contextWrapper.startService(intent);

    }
}
