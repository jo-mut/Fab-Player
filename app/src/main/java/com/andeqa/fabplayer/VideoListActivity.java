package com.andeqa.fabplayer;

import android.database.Cursor;
import android.database.MergeCursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoListActivity extends AppCompatActivity {

    @Bind(R.id.videosRecyclerView)RecyclerView videosRecyclerView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.layoutManagerImageView)ImageView mLayoutManagerImageView;

    ArrayList<HashMap<String, String>> videos = new ArrayList<HashMap<String, String>>();
    LoadAlbumImages loadAlbumTask;

    private static final String GALLERY_VIDEO ="gallery video";

    private VideosRecyclerAdapter videosRecyclerAdapter;
    private ItemOffsetDecoration itemOffsetDecoration;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static final int LINEAR = 1;
    private static final int GRID = 0;
    private boolean processManager = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle("Fab player");

        itemOffsetDecoration = new ItemOffsetDecoration(this, R.dimen.item_off_set);
        videosRecyclerAdapter = new VideosRecyclerAdapter(VideoListActivity.this, videos);
        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();
    }


    @Override
    public void onStart() {
        super.onStart();
        videosRecyclerView.addItemDecoration(itemOffsetDecoration);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        videosRecyclerView.removeItemDecoration(itemOffsetDecoration);
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
            String duration;
            String searchParams = null;
            Uri videosInternalUri = android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;
            Uri videosExternalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };

            Cursor videosCursorExternal = getContentResolver().query(videosInternalUri, projection, null, null, null);
            Cursor videosCursorInternal = getContentResolver().query(videosExternalUri, projection, null, null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{videosCursorExternal, videosCursorInternal});
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));

                MediaMetadataRetriever mdr = new MediaMetadataRetriever();
                mdr.setDataSource(path);
                duration = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                File file = new File(path);
                name = file.getName();
                videos.add(Function.mappingInbox(album, name, path, timestamp, Function.converToTime(timestamp), null, duration));
                videosRecyclerAdapter.notifyItemInserted(videos.size() - 1);
            }
            cursor.close();
            Collections.sort(videos, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            videosRecyclerView.setLayoutManager(setLayoutManager(gridLayoutManager, 0));
            videosRecyclerView.addItemDecoration(itemOffsetDecoration);

            mLayoutManagerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (processManager){
                        videosRecyclerView.setLayoutManager(setLayoutManager(linearLayoutManager, 1));
                        mLayoutManagerImageView.setBackgroundResource(R.drawable.ic_grid_layout);
                        processManager = false;
                    }else {
                        videosRecyclerView.setLayoutManager(setLayoutManager(gridLayoutManager, 0));
                        mLayoutManagerImageView.setBackgroundResource(R.drawable.ic_linear_layout);
                        processManager = true;
                    }
                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videosRecyclerView.setAdapter(null);
    }

    public RecyclerView.LayoutManager setLayoutManager(RecyclerView.LayoutManager layoutManager, int position) {
        switch (position){
            case 0:
                layoutManager = new GridLayoutManager(this, 2);
                videosRecyclerAdapter.getPosition(0);
                break;
            case 1:
                videosRecyclerAdapter.getPosition(1);
                layoutManager = new LinearLayoutManager(this);

        }

        videosRecyclerView.setHasFixedSize(false);
        videosRecyclerView.setAdapter(videosRecyclerAdapter);
        videosRecyclerView.setItemAnimator(null);

        return layoutManager;
    }
}
