package com.example.diogo.projectssui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity  {

    private ImageButton btnPlay;
    private ImageButton btnPrevious;
    private ImageButton btnShuffle;
    private ImageButton btnNext;
    private SeekBar timeLine;
    private MediaPlayer mediaPlayer = null;
    private TextView currentTime;
    private TextView totalDuration;
    private TextView tvAudioTitle;
    private AudioList audioList;

    public boolean wasPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       

        Intent intent = getIntent();
        audioList = (AudioList)intent.getSerializableExtra("AudioListClass");
        init();
        initMediaPlayer();

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
                monitorHandler.postDelayed(this, 100);//update de 100ms em 100ms
                monitorHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        durationBarUpdater();
                    }
                });
            }
        });


    }

    private Handler monitorHandler = new Handler();

    private void init() {
        // TODO Auto-generated method stub
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(Color.BLACK);
        timeLine = (SeekBar) findViewById(R.id.durationBar);
        currentTime = (TextView) findViewById(R.id.currentTime);
        totalDuration = (TextView) findViewById(R.id.totalDuration);
        btnPlay = (ImageButton)findViewById(R.id.playStopButton);
        btnPrevious = (ImageButton)findViewById(R.id.previousButton);
        btnNext = (ImageButton)findViewById(R.id.nextButton);
        btnShuffle =(ImageButton)findViewById(R.id.shuffleButton);
        tvAudioTitle = (TextView)findViewById(R.id.audioTitleText);
        tvAudioTitle.setSelected(true);
    }

    public void checkIsPlaying(){
        wasPlaying = mediaPlayer.isPlaying();
    }

    public void emptyMediaPlayer(){
        if(mediaPlayer != null){
            checkIsPlaying();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void handleNextClick(View v){
        emptyMediaPlayer();
        audioList.nextAudio();
        initMediaPlayer();
    }

    public void handlePreviousClick(View v){
        emptyMediaPlayer();
        audioList.previousAudio();
        initMediaPlayer();
    }

    public void handleShuffleClick(View v){
        if(audioList.getShuffleMode()){
            audioList.setShuffleMode(false);
            btnShuffle.setImageResource(R.drawable.shuffleoff);
            Toast.makeText(getApplicationContext(), "Shuffle Mode OFF!", Toast.LENGTH_LONG).show();
        } else {
            audioList.setShuffleMode(true);
            btnShuffle.setImageResource(R.drawable.shuffleon);
            Toast.makeText(getApplicationContext(), "Shuffle Mode ON!", Toast.LENGTH_LONG).show();
        }
    }

    public void handlePlayPauseClick(View v){
        try{
            if(mediaPlayer != null){
                if(mediaPlayer.isPlaying()) {
                    Pause();
                } else {
                    Play();
                }
            }
            else{
                initMediaPlayer();
            }
            didTapButton(v,btnPlay);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void handleReplayClick(View v){
        if (mediaPlayer != null){
        mediaPlayer.seekTo(0);
        }
    }

    public void Pause(){
        btnPlay.setImageResource(R.drawable.play);
        mediaPlayer.pause();
    }

    public void Play(){
        btnPlay.setImageResource(R.drawable.pause);
        mediaPlayer.start();
    }

    private void initMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+audioList.getCurrentAudioPath()));
        int mediaDuration = mediaPlayer.getDuration();
        timeLine.setMax(mediaDuration);
        String TotalDuration = CalculateTime(mediaDuration);
        totalDuration.setText(TotalDuration);
        tvAudioTitle.setText(audioList.getCurrentAudioName());
        btnPlay.setImageResource(R.drawable.play);
        if(mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioList.nextAudio();
                    wasPlaying = true;
                    initMediaPlayer();
                }
            });
        }
        if(wasPlaying){
            Play();
        }
    }

    private void durationBarUpdater(){ //durationbarupdater que é chamado para atualizar a durationbar e o tempo atual
        if (mediaPlayer != null){
            int mediaPosition = mediaPlayer.getCurrentPosition();
            timeLine.setProgress(mediaPosition);
            String CurrentTime = CalculateTime(mediaPosition);
            currentTime.setText(CurrentTime);
        }
    }
    public void didTapButton(View view,ImageButton btn){ //se for preciso
        final Animation bounceAnim = AnimationUtils.loadAnimation(this,R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        bounceAnim.setInterpolator(interpolator);
        btn.startAnimation(bounceAnim);
    }
    private String CalculateTime(int Time){// input is time in milliseconds
        long seconds = TimeUnit.MILLISECONDS.toSeconds(Time); //converts to seconds
        long Hours = seconds/(60*60);
        long Minutes = seconds/60;
        long Seconds = seconds %60;
        if (Hours != 0){
             return String.format("%02d:%02d:%02d",Hours,Minutes, Seconds);// formata o tempo para uma string com(hour:minute:seconds)
        } else {
            return String.format("%02d:%02d",Minutes,Seconds); //formata o tempo para uma string com(minute:seconds)
        }
    }

}