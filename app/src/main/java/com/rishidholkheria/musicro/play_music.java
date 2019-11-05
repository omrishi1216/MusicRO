package com.rishidholkheria.musicro;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class play_music extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);


        final Button pause = (Button) findViewById(R.id.pause);
        TextView song_name = (TextView) findViewById(R.id.song_name);
        ImageView album_art = (ImageView) findViewById(R.id.album_art);

       /* byte art[];

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource("/Desktop/song1.mp3");
        try {
            song_name.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            art = mmr.getEmbeddedPicture();
            Bitmap albumart = BitmapFactory.decodeByteArray(art,0,art.length);
            album_art.setImageBitmap(albumart);

        }catch (Exception e){
            album_art.setBackgroundColor(Color.GRAY);
            song_name.setText("Unknown Name");
        }

        */

        final MediaPlayer s = MediaPlayer.create(play_music.this,R.raw.song1);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.check_play){
                    s.pause();
                    pause.setBackgroundResource(R.drawable.ic_launcher_foreground);
                }else{
                    s.start();
                    pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });


    }
}
