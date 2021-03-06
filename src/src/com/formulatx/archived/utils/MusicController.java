package com.formulatx.archived.utils;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.formulatx.archived.Settings;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by alekseev on 25.01.2016.
 */
public class MusicController {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private View curView=null;
    private int curIndex=-1;
    private Activity curActivity;
    private Object syncObject=new Object();

    private Timer mTimer;

    private TextView textView;

    private boolean downTouch=false;

    public MusicController(Activity activity,  MediaPlayer player) {
        mediaPlayer=player;
        curActivity=activity;
        mTimer=new Timer();


    }


    private String getPositionInTimeFormat(int milliseconds,int duration)
    {

        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        int durationH=(int) ((duration / (1000*60*60)) % 24);

        String h="00";
                if(hours>0) {
                    h = (hours < 10) ? "0" + hours : hours + "";


                }


        String m="00";
        if(minutes>0) {
            m = (minutes < 10) ? "0" + minutes : minutes + "";


        }

        String s="00";
        if(seconds>0) {
            s= (seconds < 10) ? "0" + seconds : seconds + "";


        }

        if(durationH<1)
            return m+ ":" +s;
        else
            return h + ":" +m + ":"+ s;
    }

    public void StartTimer()
    {

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                curActivity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {Refresh();} catch (Exception ex){}
                            }
                        });
            }
        },0, Settings.REFRESH_AUDION_POSITION_TIME);
    }

    public void Refresh()
    {
        synchronized (syncObject) {
            if (!downTouch) {
                int position = mediaPlayer.getCurrentPosition();
                if (seekBar != null) {
                    seekBar.setProgress(position);
                }
                if (textView != null)
                    textView.setText(getPositionInTimeFormat(position, mediaPlayer.getDuration()));
            }
        }
    }


    public void SetBar(SeekBar bar,TextView textView)
    {

            synchronized (syncObject) {

                seekBar = bar;
                //seekBar.setBackgroundColor(Color.GRAY);
                this.textView=textView;
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser) {
                            synchronized (syncObject) {
                                mediaPlayer.seekTo(progress);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        synchronized (syncObject) {
                            downTouch=true;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        synchronized (syncObject) {
                            downTouch=false;
                        }
                    }
                });
            }


    }




}
