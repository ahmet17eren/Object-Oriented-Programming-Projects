// name surname: Ahmet Eren Aslan
// student ID: 2023400018

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    private int stageIndex;
    private final ArrayList<Stage> stages;
    public static int deathNumber = 0;
    private double gameTime;
    private double resetTime;
    private boolean isResetTime = false;
    private boolean resetGame;
    private boolean isMousePressedHelp = false;
    private boolean isMousePressedRestart;

    //constructor
    Game(ArrayList<Stage> stages){
        this.stages = stages;
    }

    //get methods for some instance variables
    public int getStageIndex(){
        return stageIndex;
    }
    public Stage getCurrentStage(){
        return stages.get(stageIndex);
    }

    private void handleInput(Map map) {
        map.movePlayer(' ');  // Apply default movement (gravity)

        if (StdDraw.isMousePressed()) { // check if mouse pressed
            double mouseX = StdDraw.mouseX(); // get x coordinate
            double mouseY = StdDraw.mouseY(); // get y coordinate

            // Check if Help button was clicked
            if ((290 > mouseX) && (mouseX > 210) && (100 > mouseY) && (mouseY > 70))
                isMousePressedHelp = true;
            // Check if Restart button was clicked
            if ((590 > mouseX) && (mouseX > 510) && (100 > mouseY) && (mouseY > 70))
                isMousePressedRestart = true;
            // Check if Reset Game button was clicked
            if ((480 > mouseX) && (mouseX > 320) && (35 > mouseY) && (mouseY > 5))
                resetGame = true;
        }

        // Check for keyboard input based on current stage's key bindings
        if (StdDraw.isKeyPressed(stages.get(stageIndex).getKeyCodes()[0])) {
            map.movePlayer('R'); // Move right
        }
        if (StdDraw.isKeyPressed(stages.get(stageIndex).getKeyCodes()[1])) {
            map.movePlayer('L'); // Move left
        }
        if (StdDraw.isKeyPressed(stages.get(stageIndex).getKeyCodes()[2])) {
            map.movePlayer('U'); // Jump
        }
    }


    public void play(){
        double startedTime = System.currentTimeMillis(); // Record game start time

        Player player = new Player(130.0,440.0); // Create player at starting position
        Map map = new Map(stages.get(getStageIndex()),player); // Create map for current stage

        // Set up the game window
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);

        // Main game loop
        while (true) {
            StdDraw.clear(); // Clear the canvas for redrawing

            gameTime = (System.currentTimeMillis() - startedTime); // Update elapsed time

            // Draw UI elements (bottom panel and buttons)
            StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 16));
            StdDraw.setPenColor(new Color(56, 93, 172)); // Color of the area
            StdDraw.filledRectangle(400, 60, 400, 60); // Drawing bottom part
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(250, 85, "Help");
            StdDraw.rectangle(250, 85, 40, 15); // Help button
            StdDraw.text(550, 85, "Restart");
            StdDraw.rectangle(550, 85, 40, 15); // Restart button
            StdDraw.text(400, 20, "RESET THE GAME");
            StdDraw.rectangle(400, 20, 80, 15); // Reset button

            // Display game stats
            StdDraw.text(700, 75, String.format("Deaths: %d" , deathNumber));
            StdDraw.text(700, 50, String.format("Stage: %s" , getStageIndex() +1));

            // Format and display game timer
            long minutes = (long)gameTime / 60000;
            long seconds = (long) (gameTime % 60000) / 1000;
            long millis = (long) (gameTime % 1000) / 10; // 2-digit milliseconds
            StdDraw.text(100, 50,String.format("%02d:%02d:%02d", minutes, seconds, millis));
            StdDraw.text(100, 75, "Level: 1");

            // Display either help or clue text based on button state
            if (isMousePressedHelp) {
                StdDraw.text(400, 85, "Help:");
                StdDraw.text(400, 55, getCurrentStage().getHelp());
            } else {
                StdDraw.text(400, 85, "Clue:");
                StdDraw.text(400, 55, getCurrentStage().getClue());
            }

            // Handle restart button click
            if (isMousePressedRestart){
                isMousePressedRestart = false;
                map.restartStage();
                deathNumber += 1;
            }

            // Handle reset game button click
            if (resetGame){
                resetGame = false;
                // Show reset notification screen
                StdDraw.setPenColor(0,240,0);
                StdDraw.filledRectangle(400,300,400,120);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new Font("Arial", Font.PLAIN, 40));
                StdDraw.textLeft(290,300,"Resetting...");
                StdDraw.show();
                StdDraw.pause(2000);
                // Reset game state
                isMousePressedHelp = false;
                startedTime = System.currentTimeMillis();
                stageIndex = 0;
                deathNumber = 0;
                map.restartStage();
            }

            // Draw game elements
            map.draw();
            handleInput(map);
            player.draw();
            map.drawPipe();

            // Check if player reached the exit pipe
            if (map.changeStage()){
                if ((stageIndex + 2) <=  stages.size()) {
                    // Display stage completion message
                    StdDraw.setPenColor(0, 255, 0);
                    StdDraw.filledRectangle(400, 300, 400, 120);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 30));
                    StdDraw.textLeft(250, 320, "You passed the stage");
                    StdDraw.textLeft(250, 290, "But is the level over?!");
                    StdDraw.show();
                    StdDraw.pause(2000);
                    // Advance to next stage
                    stageIndex += 1;
                    map = new Map(stages.get(stageIndex), player);
                    isMousePressedHelp = false;
                    player.respawn(new int[]{110, 430, 150, 450});
                    startedTime += 2000; // Adjust timer for pause duration
                } else {
                    // Game completion screen
                    StdDraw.clear();
                    StdDraw.setPenColor(0,240,0);
                    StdDraw.filledRectangle(400,300,400,120);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 35));
                    StdDraw.textLeft(20,360,"CONGRULATIONS YOU FINISHED THE LEVEL");
                    StdDraw.textLeft(150,320,"PRESS A TO PLAY AGAIN");
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 25));

                    // Calculate final completion time once
                    if (!isResetTime) {
                        resetTime = System.currentTimeMillis() -startedTime;
                        isResetTime = true;
                    }

                    // Display final stats
                    long finalMinutes = (long)resetTime / 60000;
                    long finalSeconds = (long) (resetTime % 60000) / 1000;
                    long finalMillis = (long) (resetTime % 1000) / 10;
                    StdDraw.textLeft(170, 260, String.format("you finished with %d deaths in %02d : %02d : %02d" , deathNumber , finalMinutes, finalSeconds, finalMillis));
                    StdDraw.show();

                    // Check for restart or quit commands
                    if (StdDraw.isKeyPressed(KeyEvent.VK_A)){
                        startedTime = System.currentTimeMillis();
                        stageIndex = 0;
                        deathNumber = 0;
                        isMousePressedHelp = false;
                        map.restartStage();
                    }
                    if (StdDraw.isKeyPressed(KeyEvent.VK_Q))
                        break; // Exit game loop

                }
            }

            StdDraw.show(); // Update display with all changes
            StdDraw.pause(16); // Approximately 60 FPS
        }
    }
}