package com.example.dictaphoneapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {

    private ArrayList mFileList;
    private LayoutInflater inflater;


    public FileListAdapter(Context ctx, ArrayList fileList) {

        inflater = LayoutInflater.from(ctx);
        this.mFileList = fileList;

    }


    @NonNull
    @Override
    public FileListAdapter.FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        FileListViewHolder viewHolder = new FileListViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FileListAdapter.FileListViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(mFileList.get(position)));
        holder.play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        holder.stop.setImageResource(R.drawable.ic_baseline_stop_24);
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public static class FileListViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageButton play;
        public ImageButton stop;


        public FileListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.file_name);
            play = (ImageButton) itemView.findViewById(R.id.play_image_button);
            stop = (ImageButton) itemView.findViewById(R.id.stop_image_button);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyMediaRecorder mediaRecorder = new MyMediaRecorder();
                    mediaRecorder.onPlay(true);
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyMediaRecorder mediaRecorder = new MyMediaRecorder();
                    mediaRecorder.onPlay(false);
                }
            });


        }
    }
}
