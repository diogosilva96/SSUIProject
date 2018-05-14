package com.example.diogo.projectssui;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Diogo on 14/05/2018.
 */

public class AudioList implements Serializable { //se for preciso para passar o objeto no intent
    private ArrayList<HashMap<String,String>> audioList;
    private int currentPosition;

    public AudioList(ArrayList<HashMap<String,String>> aList){
        audioList = aList;
    }


    public void setCurrentAudio(String name){
        boolean valueFound = false;
        int len = audioList.size();
        for (int i=0;i<len;i++){
            if (valueFound){
                break;
            }
            HashMap<String, String> audioFile = audioList.get(i);
            if (audioFile.get("name").equals(name)){
                valueFound = true;
                currentPosition = i;
            }
        }
        Log.e("AudioList","Value for:"+name+" found:" +valueFound + " currentPosition on List:" +currentPosition);
    }

    public void nextAudio(){
        if (currentPosition < audioList.size()) {
            currentPosition = currentPosition + 1;
        } else {
            currentPosition = 0;
        }
    }

    public String getCurrentAudioPath(){
        return audioList.get(currentPosition).get("path");
    }

    public String getCurrentAudioName(){
        Log.e("AudioList", audioList.get(currentPosition).get("name"));
        return audioList.get(currentPosition).get("name");
    }

    public void previousAudio(){
        if (currentPosition != 0){
            currentPosition = currentPosition -1;
        } else {
            currentPosition = audioList.size()-1;
        }
    }


}
