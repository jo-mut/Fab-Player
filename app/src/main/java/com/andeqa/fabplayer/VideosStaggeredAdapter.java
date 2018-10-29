package com.andeqa.fabplayer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VideosStaggeredAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private Context mContext;
    List<HashMap<String, String>> videos = new ArrayList<HashMap<String, String>>();
    private Player player;
    private static final String GALLERY_VIDEO ="gallery video";
    private static final String VIDEO_NAME = "name";



    public VideosStaggeredAdapter(Context mContext, List<HashMap<String, String>> videos ) {
        this.mContext = mContext;
        this.videos = videos;
    }


    @Override
    public long getItemId(int position) {
        int time = Integer.parseInt(videos.get(position).get(Function.KEY_TIME));
        return time;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_grid_layout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final @NonNull VideoViewHolder holder, final int position) {
        final String string = videos.get(position).get(Function.KEY_PATH);
        final String duration = videos.get(position).get(Function.KEY_DURATION);
        Glide.with(mContext)
                .load(new File(string))
                .into(holder.videoImageView);

        String title = videos.get(position).get(Function.KEY_NAME);
        if (title.length() > 15){
            String name = title.substring(0, 15);
            holder.titleTextView.setText(name +"...");
        }else {
            String name = string;
            holder.titleTextView.setText(name);
        }

        holder.durationTextView.setText(convertTime(duration));
        holder.videoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra(VideosStaggeredAdapter.GALLERY_VIDEO, videos.get(position).get(Function.KEY_PATH));
                intent.putExtra(VideosStaggeredAdapter.VIDEO_NAME, videos.get(position).get(Function.KEY_NAME));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    private String convertTime(String duration) {
        int seconds = Integer.parseInt(duration) / 1000;
        int hrs = seconds/ 3600;
        int mins = (seconds % 3600) /60;
        int secs = seconds %60;

        String time = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hrs, mins, secs);

        return time;
    }

}
