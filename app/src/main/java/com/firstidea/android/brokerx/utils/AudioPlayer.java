package com.firstidea.android.brokerx.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Govind on 20-Jan-17.
 */

public class AudioPlayer {
    private Context context;

    public AudioPlayer(Context context) {
        this.context = context;
    }

    private MediaPlayer mMediaPlayer;

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(context, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }
}
