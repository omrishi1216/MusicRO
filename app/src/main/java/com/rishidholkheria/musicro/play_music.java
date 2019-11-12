package com.rishidholkheria.musicro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;


public class play_music extends AppCompatActivity {
    final static String CHANNEL_ID = "current_playing_song";
    final int NOTIFICATION_ID = 001;
    int songPosition = 00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        final TextView song_name = findViewById(R.id.song_name);
        final TextView artist_name = findViewById(R.id.artist_name);
        final ImageView album_art = findViewById(R.id.album_art);
        final MediaPlayer mp = new MediaPlayer();
        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_bar);


        final TextView songPositionTextView = findViewById(R.id.current_position);
        final TextView songDurationTextView = findViewById(R.id.song_duration);
        final Button pause = (Button) findViewById(R.id.pause);
        final Button notification_pause = (Button) findViewById(R.id.notification_pause);

        final SeekBar seekBar = findViewById(R.id.seekbar);
        Intent i = getIntent();
        final String file_path = i.getStringExtra("file_path");


        LinearLayout LL = (LinearLayout) findViewById(R.id.layout_2);

        String[] color1 = {

                "#FF006C",
                "#00FF83",
                "#B6FF00",
                "#FF8700",
                "#0CDF69",
                "#0CB9DF"

        };
        String[] color2 = {

                "#36FF33",
                "#FFF633",
                "#FF3333",
                "#0CDFA6",
                "#0C56DF",
                "#DF0C7C"

        };
        Random r = new Random();
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,

                new int[]{Color.parseColor(color1[r.nextInt(6)]), Color.parseColor(color2[r.nextInt(6)])}

        );

        LL.setBackgroundDrawable(gd);


        if (mp.isPlaying()) {
            mp.stop();
        } else {
            try {
                mp.setDataSource(file_path);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final int songDuration = (mp.getDuration() / 1000);


        seekBar.setMax(songDuration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int songProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                songProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(songProgress);
            }
        });
        songDurationTextView.setText(String.valueOf(songDuration / 60) + ":" + String.valueOf(songDuration % 60));

        new Thread() {
            public void run() {
                songPosition = 0;
                while (songPosition < songDuration) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    songPosition++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(songPosition);
                            songPositionTextView.setText(String.valueOf(songPosition / 60) + ":" + String.valueOf(songPosition % 60));
                        }
                    });

                }
            }
        };


        byte art[] = null;

        song_name.setText("Unknown Name");
        album_art.setBackgroundColor(Color.GRAY);
        Bitmap albumart;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file_path);
        try {
            song_name.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            art = mmr.getEmbeddedPicture();
            albumart = BitmapFactory.decodeByteArray(art, 0, art.length);
            album_art.setImageBitmap(albumart);
            artist_name.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            notificationView.setImageViewBitmap(R.id.notimg, albumart);
            notificationView.setTextViewText(R.id.song_name, song_name.getText().toString());


        } catch (Exception e) {
            album_art.setBackgroundColor(Color.GRAY);
            song_name.setText("Unknown Name");
            artist_name.setText("Unknown Artist");
            notificationView.setImageViewResource(R.id.notimg, R.drawable.cd);
            notificationView.setTextViewText(R.id.song_name, "Unknown Song");
        }

        song_name.setMarqueeRepeatLimit(2);



        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    mp.pause();
                    pause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    mp.start();
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

        /*notification_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    notification_pause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    mp.start();
                    notification_pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "MusicRO Playback";
            String description = "includes_current_playing_song";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentTitle(song_name.getText().toString());
        builder.setSmallIcon(R.drawable.notification_icon)
                .setOnlyAlertOnce(true);
        //builder.setContent(notificationView);
        builder.addAction(R.id.notification_pause, "play/pause", null);
        builder.setDefaults(Notification.FLAG_NO_CLEAR)
                .setDefaults(Notification.BADGE_ICON_SMALL)
                .setDefaults(Notification.DEFAULT_LIGHTS);
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle());
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

}
