package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.model.GalleryItem;
import com.formulatx.archived.network.loaders.GalleryContentAsyncLoader;
import com.rinnion.archived.R;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.MusicController;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class PodcastContentFragment extends Fragment implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    public static String PODCAST = "podcast";
    public static final String GALLERY = GalleryContentAsyncLoader.GALLERY;
    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;

    private MediaPlayer player;
    private ListView mListView;
    private TextView timeTextView;

    private SeekBar musicBar=null;
    private MusicController musicProgressController;
    private ImageView playButton=null;
    private int posId=-1;

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
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_text_layout_with_player, null, names, to, 0);
        getLoaderManager().initLoader(R.id.gallery_loader, getArguments(), new PodcastLoaderCallback());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
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

    private void play()
    {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout_podcast, container, false);

        //mediaPlayerLayout = (LinearLayout)view.findViewById(R.id.mediaPlayerLayout);
        //controller = (MediaController)view.findViewById(R.id.mediaController);
         mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        FormulaTXApplication.setParameter("PodcastAudioId", 0L);


        player=new MediaPlayer();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(!mp.isPlaying())
                {
                    if (playButton != null)
                        playButton.setImageResource(R.drawable.play_media_icon);
                }
            }
        });
        player.setOnPreparedListener(this);

         musicProgressController=new MusicController(getActivity(),player);



       // controller=new MusicController(getActivity());
        //controller.set
        //controller=new MediaController(getActivity());
        //controller.set
        //controller=new MediaController(getActivity());
        //controller.setMediaPlayer(this);

        //controller.setFocusable(false);
        //controller.setFocusableInTouchMode(false);
      //controller.setAnchorView(mListView);
        //controller.setEnabled(true);
        //controller.setFocusable(false);
        //controller.setFocusableInTouchMode(false);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
                 GalleryHelper gh = new GalleryHelper(doh);
                GalleryItem item = gh.getItem(l);
                if (item == null) return;


                long podcastAudioId= FormulaTXApplication.getLongParameter("PodcastAudioId", 0);






                    if (playButton != null) {
                        playButton.setImageResource(R.drawable.play_media_icon);
                        if(podcastAudioId!=l) {

                            if(musicBar!=null)
                            musicBar.setVisibility(View.INVISIBLE);
                            if(timeTextView!=null)
                                timeTextView.setVisibility(View.INVISIBLE);
                        }
                    }

                musicBar = (SeekBar) view.findViewById(R.id.musicProgress);

                timeTextView=(TextView)view.findViewById(R.id.timeTextView);

                musicProgressController.SetBar(musicBar, timeTextView);


                    playButton = (ImageView) view.findViewById(R.id.playImage);











                if(podcastAudioId==l) {

                    if(!player.isPlaying()) {
                        player.start();
                        if (playButton != null)
                            playButton.setImageResource(R.drawable.pause_media_icon);
                    }
                    else {
                        player.pause();
                        if (playButton != null)
                            playButton.setImageResource(R.drawable.play_media_icon);
                    }
                    musicProgressController.Refresh();

                    musicBar.setVisibility(View.VISIBLE);
                    timeTextView.setVisibility(View.VISIBLE);
                    return;
                }
                FormulaTXApplication.setParameter("PodcastAudioId", l);
                //musicBar.setMax();

                try {
                    //String file = Files.getExternalDir("Kalimba.mp3");


                    if(player.isPlaying()) {
                        player.stop();
                        player.seekTo(0);
                    }
                    else
                    {
                        player.stop();
                        player.seekTo(0);
                    }
                    Uri tmpUri=Uri.parse(item.link);
                    //Uri tmpUri=Uri.fromFile(new File(file));
                    player.setDataSource(getActivity(), tmpUri);
                    player.prepare();
                    //controller.show();

                    player.start();
                    playButton.setImageResource(R.drawable.pause_media_icon);
                    musicProgressController.Refresh();

                    musicBar.setVisibility(View.VISIBLE);
                    timeTextView.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    musicBar.setMax(0);
                    Log.d(TAG, "MediaPlayer", e);
                    e.printStackTrace();
                }
                catch (Exception _ex)
                {
                    musicBar.setMax(0);
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
    public void onStop() {
        super.onStop();
       //controller.hide(); // So you don't get the "Leaked activity..." message.
        player.stop();

        if(playButton!=null)
        playButton.setImageResource(R.drawable.play_media_icon);





    }



    @Override
    public void onPrepared(MediaPlayer mp) {
        //controller.setMediaPlayer(this);
        Log.d(TAG, "onPrepared");


//        controller.setAnchorView(mediaPlayerLayout);
        musicBar.setMax(player.getDuration());
        musicProgressController.StartTimer();
        /*
        new Handler().post(new Runnable() {
            public void run() {



                //controller.setEnabled(true);
                //controller.show(0);
                //controller.invalidate();
            }
        });
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
        if(playButton!=null)
            playButton.setImageResource(R.drawable.play_media_icon);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        player.stop();
        player.release();
    }




    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(mp.isPlaying())
        {
            mp.stop();
            if(playButton!=null)
                playButton.setImageResource(R.drawable.play_media_icon);
        }
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

