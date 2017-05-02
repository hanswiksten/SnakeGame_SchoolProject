package com.example.hans.snake_hw;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Hans on 20-Mar-17.
 */

public class PlayerSort implements Comparator<Player> {
    private String name;
    private int score;

    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }

    //overwrites collections.sort to be able to sort player properly
    @Override
    public int compare(Player p1, Player p2) {
        Integer score1 = p1.getScore();
        Integer score2 = p2.getScore();
        //score2 - score1 to descending, reverse these for ascending
        return score2.compareTo(score1);
    }
}
