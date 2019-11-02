package com.rishidholkheria.musicro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionDetails()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;

        }
    }
}
