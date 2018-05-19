package com.example.diogo.projectssui;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Diogo on 14/05/2018.
 */

public class AudioList implements Serializable { //se for preciso para passar o objeto no intent
    private ArrayList<String> audioList;
    private int currentPosition;
    private ArrayList<Integer> playedList;//list auxiliar para o modo shuffle para saber o que é que já foi played
    private boolean Shuffle;

    public AudioList(ArrayList<String> aList){
        audioList = aList;
        playedList = new ArrayList<Integer>();
        Shuffle = false;
    }


    public void setCurrentAudio(String name){
        boolean valueFound = false;
        int len = audioList.size();
        for (int i=0;i<len;i++){
            if (valueFound){
                break;
            }

            if (getFileName(audioList.get(i)).equals(name)){
                valueFound = true;
                currentPosition = i;
            }
        }
        Log.e("AudioList","Value for:"+name+" found:" +valueFound + " currentPosition on List:" +currentPosition + " Path: "+audioList.get(currentPosition));
    }
    public void setShuffleMode(boolean shuffle){
        Shuffle = shuffle;
        if(Shuffle){
            addToPlayedList();
        }
    }

    public boolean getShuffleMode(){
        return Shuffle;
    }

    public void previousAudio(){
        addToPlayedList();
        if(!Shuffle) {
            if (currentPosition != 0) {
                currentPosition = currentPosition - 1;
            } else {
                currentPosition = audioList.size() - 1;
            }
        } else {// VERIFICAR ISTO
            int previousPosition = getPreviousPlayedAudioPosition();
            if (previousPosition != -1) {
                currentPosition = previousPosition;

            } else {
                //fica igual
            }
        }
    }

    public void nextAudio(){
        addToPlayedList();
        if(!Shuffle){
            if (currentPosition < audioList.size()-1) {
                currentPosition = currentPosition + 1;
            } else {
                currentPosition = 0;
            }
        } else {
            clearIfEndOfPlayedList();
            currentPosition = generateRandomPosition();
        }
    }

    private void addToPlayedList(){
        boolean positionExists = true;
        if(playedList.size()>0) {
            for (int i = 0; i < playedList.size(); i++) {
                Log.e("AudioList", "size:"+playedList.size()+" currentPos:" + currentPosition + " playedList.get(" + i + ")=" + playedList.get(i));
                if (playedList.get(i) == currentPosition) {
                    positionExists = true;
                    break;
                } else {
                    positionExists = false;
                }
            }
            if (!positionExists) {
                playedList.add(currentPosition);//adiciona a lista de audio que já foi reproduzido
            }
        } else {
            playedList.add(currentPosition);
        }
    }
    private int getPreviousPlayedAudioPosition(){
        int position= -1;
        if(playedList.size()>1)
        {
            playedList.remove(playedList.size()-1); //remove da playedlisto o som atual
            position = playedList.get(playedList.size()-1); //vai buscar a posição do anterior

        }
        return position;
    }


    private boolean checkPlayedList(int position){
        boolean alreadyPlayed = false;
        for (int i = 0; i < playedList.size(); i++) {
            if (position==playedList.get(i)) {
                alreadyPlayed = true;
            }
            if (alreadyPlayed) {
                break;
            }
        }
        return alreadyPlayed;
    }

    private void clearIfEndOfPlayedList(){
        if (playedList.size()==audioList.size()){
            Log.e("AudioList","playedList cleared!");
            playedList.clear();
        }
    }
    private int generateRandomPosition(){
        Random random = new Random();
        int position = 0;
        boolean PositionFound = false;
        while (!PositionFound){
            position = random.nextInt(audioList.size());
            PositionFound= !checkPlayedList(position);
        }
        return position;
    }

    public String getCurrentAudioPath(){
        return audioList.get(currentPosition);
    }

    public String getCurrentAudioName(){
        return getFileName(audioList.get(currentPosition));
    }


    public String getFileName(String path){
        String[] auxPath = path.split("/");

        int i = 0;
        while(i < auxPath.length-1){
            i++;
        }
        return auxPath[i];
    }
}
