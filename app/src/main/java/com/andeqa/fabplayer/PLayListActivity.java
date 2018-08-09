package com.andeqa.fabplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PLayListActivity extends AppCompatActivity {

    @Bind(R.id.galleryRecyclerView)RecyclerView galleryRecyclerView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    ArrayList<HashMap<String, String>> videos = new ArrayList<HashMap<String, String>>();
    LoadAlbumImages loadAlbumTask;

    private static final String GALLERY_VIDEO ="gallery video";


    private static final int VIDEO = 0;
    private static final int IMAGE = 2;

    private String mUid;
    private String mRoomId;
    private String postIntent;
    private String collectionIntent;
    private String collectionId;
    private String profileCoverIntent;
    private String profilePhotoIntent;
    private String collectionSettingsIntent;
    private String  album_name;
    private String path;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private Handler mainHandler;
    private BandwidthMeter bandwidthMeter;
    private boolean playOnClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Videos");

        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this,null));
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                mainHandler, null);
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    class LoadAlbumImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videos.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;
            String album = null;
            String name = null;
            String timestamp = null;
            String searchParams = null;
            Uri videosInternalUri = android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;
            Uri videosExternalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };

            Cursor videosCursorExternal = getContentResolver().query(videosInternalUri, projection, null, null, null);
            Cursor videosCursorInternal = getContentResolver().query(videosExternalUri, projection, null, null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{videosCursorExternal, videosCursorInternal});
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                File file = new File(path);
                name = file.getName();
                videos.add(Function.mappingInbox(album, name, path, timestamp, Function.converToTime(timestamp), null));
            }
            cursor.close();
            Collections.sort(videos, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            AlbumActivityAdapter albumActivityAdapter = new AlbumActivityAdapter(PLayListActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(PLayListActivity.this);
            galleryRecyclerView.setLayoutManager(layoutManager);
            galleryRecyclerView.setHasFixedSize(false);
            galleryRecyclerView.setAdapter(albumActivityAdapter);
        }
    }

    class AlbumActivityAdapter extends RecyclerView.Adapter<GalleryVideoViewHolder>{
        private Context mContext;
        private ArrayList<HashMap< String, String >> data;

        public AlbumActivityAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public GalleryVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_gallery_video, parent, false);
            return new GalleryVideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final @NonNull GalleryVideoViewHolder holder, final int position) {

            try {
                Glide.with(mContext)
                        .load(videos.get(position).get(Function.KEY_PATH))
                        .into(holder.videoImageView);
            }catch (Exception e){
                e.printStackTrace();
            }

            String string = videos.get(position).get(Function.KEY_NAME);
            char [] chars = string.toCharArray();
            if (chars.length > 15){
                String name = string.substring(0, 15);
                holder.titleTextView.setText(name +"...");
            }else {
                String name = string;
                holder.titleTextView.setText(name);
            }

            holder.videoRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                    intent.putExtra(PLayListActivity.GALLERY_VIDEO, videos.get(position).get(Function.KEY_PATH));
                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

    }

}
