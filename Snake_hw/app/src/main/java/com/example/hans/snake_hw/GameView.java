package com.example.hans.snake_hw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.io.File;


/**
 * Created by Hans on 11-Mar-17.
 */

public class GameView extends View implements Runnable{
    private GameEngine gameEngine;
    private GameActivity gameA;

    private boolean GameState;
    private boolean AppleState = false;

    private String playerName;
    private File filepath;
    int aheight;
    int awidth;
    int mheight;
    int mwidth;

    Context con1 = null;
    Bitmap apple;
    Bitmap mushroom;
    Bitmap grass;
    int shroomCounter = 0;
    int ShroomCalc;
    Paint textPaint =  new Paint();
    Thread animationThread = new Thread(this);

    public GameView(Context context){
        super(context);
        //Start game, get instances of classes
        gameEngine = new GameEngine();
        gameA = new GameActivity();
        gameEngine.setGameView(this);
        gameEngine.initGame();
        GameState = gameEngine.getGameState();
        //Generates bitmaps
        mushroom = BitmapFactory.decodeResource(getResources(), R.mipmap.mushroomimg);
        apple = BitmapFactory.decodeResource(getResources(), R.mipmap.appleimg);
        grass = BitmapFactory.decodeResource(getResources(), R.drawable.grass_background);
        //get widhts and heights from bitmaps
        aheight = apple.getHeight();
        awidth = apple.getWidth();
        mheight = mushroom.getHeight();
        mwidth = mushroom.getWidth();
        //other
        con1 = context;
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        //start thread
        animationThread.start();

    }

    //Start drawing to canvas
    public void onDraw(Canvas canvas){
        //Get state of game and if apple needs to spawn
        GameState = gameEngine.getGameState();
        AppleState = gameEngine.getAppleState();
        canvas.drawBitmap(grass, 0,0, null);
        canvas.drawText("Player: " + playerName + " | Score: " + gameEngine.getGameScore(), 20,50, textPaint);
        //Frame Cycle between mushroom spawn
        ShroomCalc = shroomCounter % 50;
        shroomCounter = shroomCounter + 1;
        //if game is still running
        if(GameState) {
            //Moves, draws, checks
            gameEngine.move();
            gameEngine.drawSnake(canvas);
            gameEngine.SpawnApple(canvas, apple);
            gameEngine.SpawnMushroom(canvas, mushroom);
            gameEngine.checkCollision(aheight, awidth, mheight, mwidth);
            //If true(eaten or start), randomize new location for apple
            if(AppleState){
                gameEngine.RandomizeApple();
                System.out.println("Height: " + aheight);
            }
            //If it's been 50 frame cycles, spawn another shroom
            if(ShroomCalc == 0){
                gameEngine.RandomizeMushroom();

            }

        }else{
                //Game Over
                System.out.println("Game over");
            //animationThread.interrupt();
            //gameA.quitGame(this.findViewById(android.R.id.content));
        }//end of else
    }//end of ondraw

    @Override
    public void run() {
        //checks if game is still running
        while(GameState) {
            //Draws graphics again, runs onDraw again
            postInvalidate();
            try {
                Thread.sleep(75);
            } catch (Exception e) {

            }
        }//end of while

        animationThread.interrupt();
        QuitGame();

    }//end of run



    public void QuitGame(){
        Intent intent = new Intent();
        intent.setClass(this.con1, MainActivity.class);
        con1.startActivity(intent);
        gameA.finish();

        gameA.AddPlayer(playerName, gameEngine.getGameScore(), filepath, con1);

    }

    public void setPlayerName(String name){
        playerName = name;
    }
    public void setFilePath(File file1) { filepath = file1; }




}
