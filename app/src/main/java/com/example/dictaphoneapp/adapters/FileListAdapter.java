package com.example.dictaphoneapp.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictaphoneapp.R;
import com.example.dictaphoneapp.services.DictaphonePlayerService;

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

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class FileListViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "FileListAdapterFilePath";

        private TextView textView;
        private ImageButton play;


        public FileListViewHolder(@NonNull final View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.file_name);
            play = (ImageButton) itemView.findViewById(R.id.play_image_button);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Play!");
                    startService();
                }
            });
        }

        private void startService() {


            Intent intent = new Intent(itemView.getContext(), DictaphonePlayerService.class);
            intent.putExtra(TAG, textView.getText());
            if (isServiceRunningInForeground(itemView.getContext(), DictaphonePlayerService.class)) {
                intent.setAction(DictaphonePlayerService.ACTION_PLAY);
            } else {
                intent.setAction(DictaphonePlayerService.ACTION_START);
            }
            Log.d(TAG, "startService: " + textView.getText());
            itemView.getContext().startService(intent);

        }

        private boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }

    }
}
