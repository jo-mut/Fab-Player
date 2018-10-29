package com.andeqa.fabplayer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class Player implements com.google.android.exoplayer2.Player.EventListener{
    private Context context;
    private DynamicConcatenatingMediaSource dynamicConcatenatingMediaSource;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;

    public Player(Context context,SimpleExoPlayerView playerView){
        this.context=context;
        this.dynamicConcatenatingMediaSource=new DynamicConcatenatingMediaSource();
        this.playerView=playerView;
        init_player();
    }

    private void init_player() {

        window = new Timeline.Window();
        playerView.requestFocus();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        player.addListener(this);
        playerView.setPlayer(player);

    }

    public void addMedia(String video){
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Fab Player"), bandwidthMeterA);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(video),
                dataSourceFactory, extractorsFactory, null, null);
        if(dynamicConcatenatingMediaSource.getSize()==0){
            dynamicConcatenatingMediaSource.addMediaSource(mediaSource);
            player.prepare(dynamicConcatenatingMediaSource);
        }else{
            dynamicConcatenatingMediaSource.addMediaSource(mediaSource);

        }

    }

    public void releasePlayer(){
        playerView.getPlayer().release();
    }

    public boolean player(){
        if (playerView.getPlayer() != null){

        }
        return true;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState){
            case com.google.android.exoplayer2.Player.STATE_IDLE:
                break;
            case com.google.android.exoplayer2.Player.STATE_BUFFERING:
                break;
            case com.google.android.exoplayer2.Player.STATE_READY:
                break;
            case com.google.android.exoplayer2.Player.STATE_ENDED:
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
