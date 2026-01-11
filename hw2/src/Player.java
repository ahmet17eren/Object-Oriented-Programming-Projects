// name surname: Ahmet Eren Aslan
// student ID: 2023400018

import java.awt.event.KeyEvent;

public class Player {
    private double x;
    private double y;
    private final double width = 20;
    private final double height = 20;
    public double velocityY;
    private boolean directionRight = true;

    //constructor
    Player(double x, double y){
        this.x = x;
        this.y = y;
    }



    public void draw(){
        // Update facing direction based on key presses
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
            directionRight = true;
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
            directionRight = false;

        // Draw player sprite based on current direction
        if (directionRight)
            StdDraw.picture(x,y,"misc/ElephantRight.png",width,height);
        else
            StdDraw.picture(x,y,"misc/ElephantLeft.png",width,height);
    }

    // getter and setter methods
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public void respawn(int[] spawnPoint){
        // Reset player position to spawn point when died or level restarted
        setX((spawnPoint[0] + spawnPoint[2])/2.0);
        setY((spawnPoint[1] + spawnPoint[3])/2.0 -20);
        draw();
    }

}
