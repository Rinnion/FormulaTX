package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.network.loaders.GalleryContentAsyncLoader;
import com.rinnion.archived.utils.Files;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.utils.MusicController;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class PodcastContentFragment extends Fragment implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    public static String PODCAST = "podcast";
    public static final String GALLERY = GalleryContentAsyncLoader.GALLERY;
    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private MediaController controller;
    private MediaPlayer player;
    private int seek_position = 0;

    private boolean isPaused = false; // If the player was paused before being stopped.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        String[] names = new String[]{GalleryHelper._ID};
        int[] to = new int[]{R.id.itl_tv_text};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_text_layout, null, names, to, 0);
        getLoaderManager().initLoader(R.id.gallery_loader, getArguments(), new PodcastLoaderCallback());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
            Bundle args = getArguments();
            if (args == null) throw new IllegalStateException("No arguments");
            long gid = getArguments().getLong(GALLERY);
            GalleryHelper gh=new GalleryHelper(doh);
            GalleryDescriptionCursor.GalleryDescription gallery = gh.getGallery(gid);
            ab.setTitle(gallery.title);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout_podcast, container, false);
         controller = (MediaController)view.findViewById(R.id.mediaController);
        ListView mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        player=new MediaPlayer();
        player.setOnPreparedListener(this);

        //controller=new MusicController(getActivity());
        //controller.set
        controller.setMediaPlayer(this);
      //controller.setAnchorView(mListView);
        controller.setEnabled(true);




        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
                GalleryHelper gh = new GalleryHelper(doh);
                GalleryItem item = gh.getItem(l);
                if (item == null) return;


                try {
                    String file = Files.getExternalDir("Kalimba.mp3");

                    if(player.isPlaying()) {
                        player.stop();
                        player.seekTo(0);
                    }
                    //Uri tmpUri=Uri.parse(item.link);
                    Uri tmpUri=Uri.fromFile(new File(file));
                    player.setDataSource(getActivity(),tmpUri);
                    player.prepare();
                    //controller.show();
                    player.start();

                } catch (IOException e) {
                    Log.d(TAG, "MediaPlayer", e);
                    e.printStackTrace();
                }
                catch (Exception _ex)
                {
                    Log.e(TAG,"Error prepare MediaPlayer",_ex);

                }


//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                URI uri = URI.create(item.link);
//                File file = new File(uri);
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                startActivity(intent);
                //Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(item.link));
                //startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void start() {
        player.start();
        this.isPaused=false;
    }

    @Override
    public void pause() {
        player.pause();
        this.isPaused=true;

    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        try {


            return player.getCurrentPosition();
        }
        catch (Exception ex){}
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.hide(); // So you don't get the "Leaked activity..." message.
        player.stop();


    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //controller.setMediaPlayer(this);
        Log.d(TAG, "onPrepared");
        controller.setMediaPlayer(this);
        //mediaController.setAnchorView(findViewById(R.id.main_audio_view));


        new Handler().post(new Runnable() {
            public void run() {
                controller.setEnabled(true);
                controller.show(10000);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controller.hide();
        player.stop();
        player.release();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    private class PodcastLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryContentAsyncLoader(getActivity(), args, GalleryHelper.TYPE_AUDIO);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }
}

