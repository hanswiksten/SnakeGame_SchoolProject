package com.example.hans.snake_hw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Created by Hans on 12-Mar-17.
 */

public class GameActivity extends AppCompatActivity{
    ArrayList<Player> Players = null;
    private String playerName = "";
    int index1;
    GameView gv;
    private File filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = new GameView(this);
        filepath = getFilesDir();
        setContentView(R.layout.activity_game);
        StartGame();
    }

    public void StartGame(){
        playerName = (String) getIntent().getStringExtra("playerName");
        System.out.println("Test; Player: -> " + playerName);
        gv.setPlayerName(playerName);
        gv.setFilePath(filepath);
        setContentView(gv);
    }
/*
    public String getPlayerName(){
        return playerName;
    }
*/
    //Calls for input values, filepath and recent context to point at
    void AddPlayer(String name1, int score1, File path1, Context context){
        //get and init values
        boolean doesntExists = true;
        File f =  new File(path1, "Players");
        //if file doesn't exist, create new list
        if(f.exists() == false){
            Players = new ArrayList();
            //Else if it exists open stream to file
        }else {
            try {
                FileInputStream fis = context.openFileInput("Players");
                ObjectInputStream obs = new ObjectInputStream(fis);
                Players = (ArrayList<Player>)obs.readObject();
            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
        //Get user input and add to arraylist
        System.out.println(name1 + " " + score1);

        if(name1 != "") {
            //cycle through all player values
            for (Player w : Players) {
                //checks if arraylist contains name1, if it does, break out
                if (name1.equalsIgnoreCase(w.getName())) {
                    doesntExists = false;
                    System.out.println("Found a match!");
                    index1 = Players.indexOf(Players);
                    System.out.println("Index -> " + index1);

                }
                //check if found a match
                if(!doesntExists){
                    //if found, compare score, if greater edit new
                    if(w.getScore() < score1) {
                        w.setScore(score1);
                    }
                    //stop loop
                    break;
                }
            }//end of for compare
        }//end of if name is empty

        if(doesntExists) {
            Players.add(new Player(name1, score1));
        }

        if(name1 != "" && name1 != null) {
            //Send data to file
            try {

                FileOutputStream fos = context.openFileOutput("Players", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(Players);
                fos.close();
                oos.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }// end of if nameinsert
    }//end of addplayer


}
