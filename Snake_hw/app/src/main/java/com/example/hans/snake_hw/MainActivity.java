package com.example.hans.snake_hw;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{
    private boolean fexists = false;
    private ArrayList<Player> Players = null;
    String playerName;
    /*
    *
    * Note:
    * This was only tested with an android phone running android 6.0, could have issues with previous versions
    *
    * How to play:
    * Write name(optional, recommended), Press Start
    * Move by touching the screen, avoid hitting walls and mushrooms
    * try to "eat"/hit apples to gain score
    *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showHighScore();
    }


    public void startGameView(View v){
        EditText et = (EditText) findViewById(R.id.nameInsert);
        playerName =  et.getText().toString();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        finish();

    }

    //Checks if Players file exists, if exists, iterate and show in list all of results
    //Send playername to gameview somehow, make gameview on game end add player and score
    public void showHighScore(){

        File f = new File(getFilesDir(), "Players");
        if(f.exists()){
            try {
                fexists = true;
                FileInputStream fis = openFileInput("Players");
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<Player> Players = (ArrayList<Player>) ois.readObject();
                this.Players = Players;
                fis.close();
                ois.close();
                //sorts list, highest score to lowest
                Collections.sort(Players, new PlayerSort());

                ListView highscoreLV = (ListView) findViewById(R.id.HighScoreList);
                String[] highscoreInfo = new String[Players.size()];
                for(int i  = 0; i < Players.size(); i++){
                    highscoreInfo[i] = Players.get(i).getName() + " | " + Players.get(i).getScore();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, highscoreInfo);
                highscoreLV.setAdapter(adapter);

            }//end of try
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());

            }//end of catch
        }//end of if exists
    }//end of highscore
}//end of main

