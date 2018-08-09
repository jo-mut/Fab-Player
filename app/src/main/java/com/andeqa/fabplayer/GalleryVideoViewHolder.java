package com.andeqa.fabplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class GalleryVideoViewHolder  extends RecyclerView.ViewHolder {
    Context context;
    View view;
    public ImageView videoImageView;
    public TextView durationTextView;
    public TextView titleTextView;
    public RelativeLayout videoRelativeLayout;

    public GalleryVideoViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        view = itemView;
        videoImageView = (ImageView) view.findViewById(R.id.videoImageView);
        durationTextView = (TextView) view.findViewById(R.id.durationTextView);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        videoRelativeLayout = (RelativeLayout) view.findViewById(R.id.videoRelativeLayout);
    }
}
