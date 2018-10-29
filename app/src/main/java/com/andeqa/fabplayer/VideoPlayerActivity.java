package com.andeqa.fabplayer;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {

    @Bind(R.id.player_view)SimpleExoPlayerView exoPlayerView;
    @Bind(R.id.titleTextView)TextView titleTextView;

    private Player player;
    private String video;
    private String name;
    private static final String GALLERY_VIDEO ="gallery video";
    private static final String VIDEO_NAME = "name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null){
            video = getIntent().getStringExtra(GALLERY_VIDEO);
            name = getIntent().getStringExtra(VIDEO_NAME);

            titleTextView.setText(name);

            if (exoPlayerView.getPlayer() != null){
                exoPlayerView.setPlayer(null);
                if (exoPlayerView.getPlayer() == null){
                    player = new Player(getApplicationContext(), exoPlayerView);
                    player.addMedia(video);
                    exoPlayerView.setVisibility(View.VISIBLE);
                }
            }else {
                player = new Player(getApplicationContext(), exoPlayerView);
                player.addMedia(video);
                exoPlayerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exoPlayerView.getPlayer().setPlayWhenReady(true);
            }
        },1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayerView.getPlayer().setPlayWhenReady(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayerView.getPlayer().setPlayWhenReady(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exoPlayerView.getPlayer().setPlayWhenReady(true);
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayerView.getPlayer().release();
    }
}
