package com.example.diogo.projectssui;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity  {

    private SurfaceView surfaceView;
    private Button btnPlay;
    private SeekBar timeLine;
    private MediaPlayer mediaPlayer = null;
    private TextView currentTime;
    private TextView totalDuration;
    private Button btnNext;
    private int[] videos;
    private int currentArrayVideoPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVideoSet();
        init();
        timeLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int selectedTime = timeLine.getProgress();
                    mediaPlayer.seekTo(selectedTime);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                monitorHandler.postDelayed(this, 250);//update de 250ms em 250ms
                monitorHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayerMonitor();
                    }
                });
            }
        });


    }

    private Handler monitorHandler = new Handler();

    private void init() {
        // TODO Auto-generated method stub
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        timeLine = (SeekBar) findViewById(R.id.durationBar);
        timeLine.setVisibility(View.INVISIBLE);
        currentTime = (TextView) findViewById(R.id.currentTime);
        totalDuration = (TextView) findViewById(R.id.totalDuration);
    }

    public void onClickBtnNext(View v){


            if (currentArrayVideoPos +1 < videos.length){
                currentArrayVideoPos = currentArrayVideoPos +1;
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                }
                Toast.makeText(getApplicationContext(), ""+currentArrayVideoPos, Toast.LENGTH_LONG).show();
                initVideoPlayer();
            }
    }

    public void PlayStop(View v){
        try{
            if(mediaPlayer != null){
                if(mediaPlayer.isPlaying()) {
                    btnPlay.setText("Play");
                    mediaPlayer.pause();
                    Toast.makeText(getApplicationContext(), "Pausing!", Toast.LENGTH_LONG).show();
                } else {
                    btnPlay.setText("Pause");
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                initVideoPlayer();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initVideoPlayer(){
        getWindow().setFormat(PixelFormat.UNKNOWN);
        mediaPlayer = MediaPlayer.create(this, videos[currentArrayVideoPos]);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        timeLine.setVisibility(View.VISIBLE);
        //surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer.setDisplay(surfaceHolder);
        int mediaDuration = mediaPlayer.getDuration();
        timeLine.setMax(mediaDuration);
        String TotalDuration = CalculateTime(mediaDuration);
        totalDuration.setText(TotalDuration);
        mediaPlayer.start();
        btnPlay.setText("Pause");
        Toast.makeText(getApplicationContext(), "Started video play", Toast.LENGTH_LONG).show();
    }

    private void initVideoSet(){
        videos = new int[2];
        videos[0] = R.raw.video1;
        videos[1] = R.raw.videoplayback;
        currentArrayVideoPos = 0;
    }


    private void mediaPlayerMonitor(){ //monitor que é chamado para atualizar a view
        if (mediaPlayer != null){
            //int mediaDuration = mediaPlayer.getDuration();
            int mediaPosition = mediaPlayer.getCurrentPosition();
            //timeLine.setMax(mediaDuration);
            timeLine.setProgress(mediaPosition);
            String CurrentTime = CalculateTime(mediaPosition);
            currentTime.setText(CurrentTime);
        }
    }

    private String CalculateTime(int Time){// input is time in milliseconds
        long seconds = TimeUnit.MILLISECONDS.toSeconds(Time); //converts to seconds
        String time =secondsToString(seconds);
        return time;
    }

    private String secondsToString(long timeInSeconds){
        return String.format("%02d:%02d:%02d",timeInSeconds/(60*60),timeInSeconds /60, timeInSeconds %60);// formata o tempo para uma string com(hour:minute:seconds)
    }
}