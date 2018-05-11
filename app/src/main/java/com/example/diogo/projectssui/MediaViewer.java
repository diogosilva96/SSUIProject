package com.example.diogo.projectssui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaViewer extends AppCompatActivity {

    private static final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    private static String MODULE = "MediaViewer Activity";
    private ListView audioListView;
    private TextView titleText;
    private ArrayList<String> audioListPath = new ArrayList<>();
    private ArrayList<String> audioListName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_viewer);
        audioListView = findViewById(R.id.audioList);
        titleText = findViewById(R.id.titleText);
        audioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), audioListPath.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
        getAllAudioMedia();
        Log.e("Audio","AudioList size:"+audioListPath.size());
        //Log.e("Audio","AudioList 1:"+audioList.get(0).get("file_path"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,audioListName);
        audioListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getAllAudioMedia(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};//dá só o display name
        String[] proj = {MediaStore.Audio.Media.DATA};

        Cursor cursor = contentResolver.query(uri, proj, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            do {
                addToAudioList(cursor);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getAllAudioMedia();
                } else {
                    // Permission Denied
                    Toast.makeText(MediaViewer.this, "READ EXTERNAL STORAGE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public String getFileName(String path){
        String[] auxPath = path.split("/");

        int i = 0;
        while(i < auxPath.length-1){
            i++;
        }
        String filename = auxPath[i];
        return filename;
    }

    public void addToAudioList(Cursor cursor){
        String path = cursor.getString(0);
        audioListPath.add(path);
        audioListName.add(getFileName(path));
    }
}
