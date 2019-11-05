package com.rishidholkheria.musicro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Random;
import java.util.zip.Inflater;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    public static boolean check_play = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout);

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

        relativeLayout.setBackgroundDrawable(gd);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    private  static final String[] PERMISSIONS = {

            Manifest.permission.READ_EXTERNAL_STORAGE

    };

    private static final int REQUEST_PERMISSIONS = 1001;

    private  static final int PERMISSIONS_COUNT = 1;

    private boolean PermissionDetails(){
        for(int i = 0;i < PERMISSIONS_COUNT;i++){

            if(checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(PermissionDetails()){
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }else {
            onResume();
        }
    }

    private boolean isMusicPlayerInit;

    private List<String> musicFilesList;

    private void addMusicFilesFrom(String dirPath){
        final File musicDir = new File(dirPath);
        if(!musicDir.exists()){
            musicDir.mkdir();
            return;
        }
        final File[] files = musicDir.listFiles();

        for(File file : files){
            final String path = file.getAbsolutePath();

            if(path.endsWith(".mp3")){
                musicFilesList.add(path);
            }
        }
    }

      private void fillMusicList(){
        musicFilesList.clear();
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)));
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
    }

    final MediaPlayer mp = new MediaPlayer();
    private void playMusicFile(String path){

        try{
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionDetails()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;

        }

       if(!isMusicPlayerInit){
           final ListView musiclist = (ListView) findViewById(R.id.musiclist);
           final TextAdapter textAdapter = new TextAdapter();
           musicFilesList = new ArrayList<>();
           fillMusicList();
           textAdapter.setData(musicFilesList);
           musiclist.setAdapter(textAdapter);
           musiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   if(mp.isPlaying()){
                       mp.pause();
                   }
                   final String musicFilePath = musicFilesList.get(position);
                   playMusicFile(musicFilePath);

                   Intent i = new Intent(getApplicationContext(),play_music.class);
                   i.putExtra("file_path",musicFilePath);
                   startActivity(i);


               }
           });

           isMusicPlayerInit = true;
       }
    }

    class TextAdapter extends BaseAdapter{

        private List<String> data = new ArrayList<>();

        void setData(List<String> mData){
            data.clear();
            data.addAll(mData);

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.song, parent, false);
                convertView.setTag(new ViewHolder((TextView)convertView.findViewById(R.id.song)));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            final String song = data.get(position);

            holder.info.setText(song.substring(song.lastIndexOf('/')+1));
            return convertView;
        }
        class ViewHolder{
            TextView info;

            ViewHolder(TextView minfo){
                info = minfo;
            }
        }
    }

}
