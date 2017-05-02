package com.example.hans.snake_hw;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * Created by Hans on 11-Mar-17.
 * Handles the majority of game functions and elements
 */

public class GameEngine{
    //List to hold all snake coordinates
    private List<Coordinate> Snake = new ArrayList<>();
    private List<MCoordinate> Mushrooms = new ArrayList<>();
    //positions
    private int width = 50;
    private int height = 50;

    private int randAX = 0;
    private int randAY = 0;

    private int randMX = 0;
    private int randMY = 0;

    private int TouchX;
    private int TouchY;
    //base score
    private int gameScore = -1;
    //paints
    Paint sPaint = new Paint();
    Paint aPaint = new Paint();
    //other
    private GameView gv;
    //if this is true, game is running, if false, player lost
    private boolean GameState = true;
    //base speed
    private int bspeed = 30;
    //true false
    private boolean touched = false;
    private boolean AppleState = true;

    //Setters and getters
    public void setGameView(GameView gv1){
        gv = gv1;
    }
    public boolean getGameState(){
        return GameState;
    }
    public boolean getAppleState(){ return AppleState; }
    public int getGameScore(){ return gameScore; }


    //Spawns initial snake, sets colors
    public void initGame(){
        AddSnakePart(250,200, bspeed, 0);
        AddSnakePart(220,200, bspeed, 0);
        AddSnakePart(190,200, bspeed, 0);
        AddSnakePart(160,200, bspeed, 0);
        sPaint.setColor(Color.RED);
        aPaint.setColor(Color.MAGENTA);
        GameState = true;
    }

    //adds another snake part
    public void AddSnakePart(int x, int y, int xspeed, int yspeed){
        Snake.add(new Coordinate(x,y, xspeed, yspeed));
    }

    //draws all snake parts
    public void drawSnake(Canvas canvas){
        for(int i = 0; i < Snake.size(); i++){
            RectF rect = new RectF(Snake.get(i).getX(), Snake.get(i).getY(), Snake.get(i).getX()+width, Snake.get(i).getY()+height);
            canvas.drawOval(rect, sPaint);
        }
    }


    public void move(){
        //Moves all snakes according to their speed and position
        //moves snake tails
        for(int i = Snake.size()-1; i > 0;){
            //gets coordination of next part
            int nextX = Snake.get(i-1).getX();
            int nextY = Snake.get(i-1).getY();
            //sets coordinate of next part to current
            Snake.get(i).setX(nextX);
            Snake.get(i).setY(nextY);
            i--;
        }
        //Moves snake head
        Snake.get(0).setX(Snake.get(0).getX() + Snake.get(0).getXspeed());
        Snake.get(0).setY(Snake.get(0).getY() + Snake.get(0).getYspeed());


        //Checks for user clicks and readjusts speeds/direction accordingly
        gv.setOnTouchListener(new View.OnTouchListener()
        {
            //Actives on touch
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN)
                {
                    //Testing
                    /*
                    System.out.println("Snake1: " + Snake.get(0).getX() + " " + Snake.get(0).getY() );
                    System.out.println("Snake2: " + Snake.get(1).getX() + " " + Snake.get(1).getY() );
                    System.out.println("Snake3: " + Snake.get(2).getX() + " " + Snake.get(2).getY() );
                    System.out.println("Snake4: " + Snake.get(3).getX() + " " + Snake.get(3).getY() );
                    */
                    //gets coordinates for touch
                    TouchX = (int) event.getX();
                    TouchY = (int) event.getY();
                    touched = true;

                }
                return true;
            }
        });

        //Checks if touched is true, adjust direction
        if(touched){
            int x =  TouchX - Snake.get(0).getX();
            int y =  TouchY - Snake.get(0).getY();
            //Calcs sqr root of difference between x^2 + different between y^2
            double h = Math.floor(Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));
            int newxspeed = (int) Math.floor(x/h * bspeed);
            int newyspeed = (int) Math.floor(y/h * bspeed);
            //Set new direction
            Snake.get(0).setXspeed(newxspeed);
            Snake.get(0).setYspeed(newyspeed);
            //set false until new touch
            touched = false;


        }

    }//end of move



    //Collision check
    public void checkCollision(int h, int w, int h2, int w2){
        //walls
            //collides with right wall
            if(Snake.get(0).getX() > gv.getWidth()){
                GameState = false;
                //System.out.println("Game Over, Right");
            }
            //Collides with left wall
            if(Snake.get(0).getX() < 0){
                GameState = false;
                //System.out.println("Game Over, Left");
            }
            //Collides with bottom wall
            if(Snake.get(0).getY() > gv.getHeight()){
                GameState = false;
                //System.out.println("Game Over, down");
            }
            //Collides with top wall
            if(Snake.get(0).getY() < 0){
                GameState = false;
                //System.out.println("Game Over, Up");
            }
        //end of wall collision

        //Check apple & mushroom collision
        //Not 100% accurate but close enough.
        int SnakeHeadX = Snake.get(0).getX();
        int SnakeHeadY = Snake.get(0).getY();

        //apple
        //Old inaccurate -> SnakeHeadX - (randAX+30)) < width && Math.abs(SnakeHeadY- (randAY+30)) < height
        int xDiff = ((SnakeHeadX + SnakeHeadX+width)/2) - ((randAX + randAX+w)/2);
        int yDiff = ((SnakeHeadY + SnakeHeadY+height)/2) - ((randAY + randAY+h)/2);
        if(Math.abs(xDiff) < 50 && Math.abs(yDiff)  < 50){
            //Test System.out.println("Difference: " + Math.abs(SnakeHeadX - randAX) + " X : Y " + Math.abs(SnakeHeadY - randAY));
            System.out.println("X " + xDiff + " Y " + yDiff);
            RandomizeApple();
        }
        //old inaccurate -> Math.abs(SnakeHeadX - (Mushrooms.get(i).getX() + 30)) < width && Math.abs(SnakeHeadY - (Mushrooms.get(i).getY() + 30)) < height
        //mushroom
        for(int i = 0; i < Mushrooms.size(); i++) {
            int xDiff2 = ((SnakeHeadX + SnakeHeadX+width)/2) - ((Mushrooms.get(i).getX() + Mushrooms.get(i).getX()+w2)/2);
            int yDiff2 = ((SnakeHeadY + SnakeHeadY+height)/2) - ((Mushrooms.get(i).getY() + Mushrooms.get(i).getY()+h2)/2);
            if (Math.abs(xDiff2) < 50 && Math.abs(yDiff2) < 50) {
                GameState = false;
            }
        }

    }//end of check collision


    public void RandomizeApple(){
        Random rand = new Random();
        int numX = gv.getWidth();
        int numY = gv.getHeight();
        //System.out.println("Test " + numX + " " + numY);
        randAX = rand.nextInt(numX);
        randAY = rand.nextInt(numY);
        AppleState = false;
        //Checks to ensure spawn isn't inside a wall
        if(randAX <= 50){
            randAX =+ 50;
        }
        if(randAX >= (numX-50)){
            randAX =- 50;
        }
        if(randAY <= 50){
            randAY =+ 50;
        }
        if(randAY >= (numY-50)){
            randAY =- 50;
        }
        //add new snake tail after eating
        int SnakeLength = Snake.size();
        AddSnakePart((Snake.get(SnakeLength-1).getX()-10),(Snake.get(SnakeLength-1).getY()-10),bspeed, 0);
        gameScore = gameScore +1;
    }

    public void SpawnApple(Canvas canvas, Bitmap apple){
        //RectF rect = new RectF(randX, randY, randX+width, randY+height);
       // canvas.drawOval(rect, sPaint);

        canvas.drawBitmap(apple, randAX, randAY, null);

    }

    public void RandomizeMushroom(){
        Random rand = new Random();
        int numX = gv.getWidth();
        int numY = gv.getHeight();
        randMX = rand.nextInt(numX);
        randMY = rand.nextInt(numY);
        //Checks to ensure spawn isn't inside a wall
        if(randMX <= 50){
            randMX =+ 50;
        }
        if(randMX >= (gv.getWidth()-50)){
            randMX =- 50;
        }
        if(randMY <= 50){
            randMY =+ 50;
        }
        if(randMY >= (gv.getHeight()-50)){
            randMY =- 50;
        }
        //Add new mushroom to arraylist
        Mushrooms.add(new MCoordinate(randMX, randMY));

    }

    //draw mushrooms
    public void SpawnMushroom(Canvas canvas, Bitmap mushroom){
        for(int i = 0; i < Mushrooms.size(); i++) {
            canvas.drawBitmap(mushroom, Mushrooms.get(i).getX(), Mushrooms.get(i).getY(), null);
        }
    }

}
