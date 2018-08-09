package com.andeqa.fabplayer;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    private Player player;
    private Timeline.Window window;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory  extractorsFactory;
    private MediaSource mediaSource;
    private TrackSelection.Factory tractSelectionFactory;
    private TrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;
    private RenderersFactory renderersFactory;
    private Handler mainHandler;
    private LoadControl loadControl;
    private String video;
    private Uri uri;
    private static final String GALLERY_VIDEO ="gallery video";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null){
            video = getIntent().getStringExtra(GALLERY_VIDEO);
            player = new Player(getApplicationContext(), exoPlayerView);
            player.addMedia(video);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        exoPlayerView.getPlayer().setPlayWhenReady(true);
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
        exoPlayerView.getPlayer().setPlayWhenReady(true);
    }
}
