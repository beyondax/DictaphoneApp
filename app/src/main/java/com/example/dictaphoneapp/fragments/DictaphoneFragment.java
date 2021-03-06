package com.example.dictaphoneapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictaphoneapp.R;
import com.example.dictaphoneapp.adapters.FileListAdapter;
import com.example.dictaphoneapp.model.MyMediaModel;
import com.example.dictaphoneapp.services.DictaphonePlayerService;
import com.example.dictaphoneapp.services.DictaphoneRecorderService;

import java.util.ArrayList;


public class DictaphoneFragment extends Fragment {

    public static final String FOREGROUND_SERVICE = "ForegroundService";
    private static final String TAG = "DictaphoneFragment";
    private Button mStartRecordingButton;
    private Button mStopRecordingButton;
    private ImageButton mStopPlayingButton;
    private RecyclerView mFilesRecyclerView;
    private RecyclerView.Adapter<FileListAdapter.FileListViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyMediaModel mMyMediaModel = new MyMediaModel();
    private ArrayList<String> mFilePathList = new ArrayList<>();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshUIAfterRecordingStopped();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictaphone, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("ACTION_CLOSE"));

        mMyMediaModel.getFilesList(mFilePathList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new FileListAdapter(getActivity(), mFilePathList);

        mStartRecordingButton = view.findViewById(R.id.start_recording_button);
        mStopRecordingButton = view.findViewById(R.id.stop_recording_button);

        mStopPlayingButton = view.findViewById(R.id.stop_image_button);

        mFilesRecyclerView = view.findViewById(R.id.files_recycler_view);
        mFilesRecyclerView.setHasFixedSize(true);
        mFilesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mFilesRecyclerView.setLayoutManager(mLayoutManager);
        mFilesRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "mFileListArray: " + mFilePathList);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStopRecordingButton.setEnabled(false);

        mStartRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
                recorderServiceOnButtonsConfig();
            }

        });

        mStopRecordingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopService();
                refreshUIAfterRecordingStopped();

            }
        });

        mStopPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPlaying();

            }
        });

    }

    private void startService() {
        Intent intent = new Intent(getActivity(), DictaphoneRecorderService.class);
        intent.putExtra(TAG, FOREGROUND_SERVICE);
        getActivity().startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(getActivity(), DictaphoneRecorderService.class);
        intent.setAction(DictaphoneRecorderService.ACTION_CLOSE);
        getActivity().startService(intent);
    }

    private void stopPlaying() {
        Intent intent = new Intent(getActivity(), DictaphonePlayerService.class);
        intent.setAction(DictaphonePlayerService.ACTION_STOP);
        getActivity().startService(intent);
    }

    private void recorderServiceOnButtonsConfig() {
        mStartRecordingButton.setEnabled(false);
        mStartRecordingButton.setText(R.string.recording_button_pressed_title);
        mStopRecordingButton.setEnabled(true);
    }

    private void refreshUIAfterRecordingStopped() {
        mStartRecordingButton.setEnabled(true);
        mStartRecordingButton.setText(R.string.recording_button_default_title);
        mStopRecordingButton.setEnabled(false);

        mFilePathList.clear();
        mMyMediaModel.getFilesList(mFilePathList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();

    }


}
