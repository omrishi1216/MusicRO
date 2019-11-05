package com.rishidholkheria.musicro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Random;

public class play_music extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        LinearLayout LL = (LinearLayout) findViewById(R.id.layout_2);

        String [] color1 = {
                "#89216B",
                "#3c1053",
                "#4b134f",
                "#642B73",
                "#45a247",
                "#8e44ad"

        };
        String [] color2 = {

                "#DA4453",
                "#ad5389",
                "#C6426E",
                "#159957",
                "#1CB5E0",
                "#1CB5E0"

        };
        Random r = new Random();
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,

                new int[]{Color.parseColor(color1[r.nextInt(6)]),Color.parseColor(color2[r.nextInt(6)])}

        );

        LL.setBackgroundDrawable(gd);

        Intent i = getIntent();
        final String file_path = i.getStringExtra("file_path");

        final Button pause = (Button) findViewById(R.id.pause);
        TextView song_name = (TextView) findViewById(R.id.song_name);
        ImageView album_art = (ImageView) findViewById(R.id.album_art);

        byte art[];

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file_path);
        try {
            song_name.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            art = mmr.getEmbeddedPicture();
            Bitmap albumart = BitmapFactory.decodeByteArray(art,0,art.length);
            album_art.setImageBitmap(albumart);

        }catch (Exception e){
            album_art.setBackgroundColor(Color.GRAY);
            song_name.setText("Unknown Name");
        }




/*          pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.check_play){
                    s.pause();
                    pause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                }else{
                    s.start();
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

*/
    }
}
