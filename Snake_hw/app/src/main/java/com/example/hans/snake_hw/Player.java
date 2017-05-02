package com.example.hans.snake_hw;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Hans on 13-Mar-17.
 */

public class Player implements Serializable{
    private String name;
    private int score;

    public Player(String inName, int inScore){
        this.name = inName;
        this.score = inScore;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    void setScore(int s){
        this.score = s;
    }

}
