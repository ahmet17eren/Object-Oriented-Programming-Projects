// name surname: Ahmet Eren Aslan
// student ID: 2023400018

import java.util.Random;

public class Map {
    private final Stage stage;
    private final Player player;
    private int buttonPressNum;
    private boolean isDoorOpen = false;
    private final int[][] obstacles = {
            new int[]{0, 120, 120, 270}, new int[]{0, 270, 168, 330},
            new int[]{0, 330, 30, 480}, new int[]{0, 480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0, 120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}};
    private final  int[] button = new int[]{400, 390, 470, 410};
    private final int[] buttonFloor = new int[]{400, 390, 470, 400};
    // Start Pipe Coordinates for Drawing
    private final int[][] startPipe = {new int[]{115, 450, 145, 480},
            new int[]{110, 430, 150, 450}};
    // Exit Pipe Coordinates for Drawing
    private final int[][] exitPipe = {new int[]{720, 175, 740, 215},
            new int[]{740, 180, 770, 210}};
    // Coordinates of spike areas
    private final int[][] spikes = {
            new int[]{30, 333, 50, 423}, new int[]{121, 150, 207, 170},
            new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
            new int[]{750, 301, 769, 419}, new int[]{680, 490, 710, 510},
            new int[]{401, 550, 521, 570}};
    // Timer Area (Blue Area at the Bottom)
    private final int[] timerArea = new int[]{0, 0, 800, 120}; // Door Coordinates
    private final int[] door = new int[]{685, 180, 700, 240};
    boolean isOnButton = false;


    //constructor
    Map( Stage stage, Player player){
        this.stage = stage;
        this.player = player;
    }

    //get methods for some instance variables
    public Stage getState(){
        return stage;
    }
    public Player getPlayer(){
        return player;
    }

    /**
     * Checks if the given coordinates collide with any obstacles
     * @param nextX The potential X coordinate to check
     * @param nextY The potential Y coordinate to check
     * @param obstacles Array of obstacle boundaries to check against
     * @return true if collision detected, false otherwise
     */
    private boolean checkCollision(double nextX, double nextY, int[][] obstacles){
        // Check collision with all obstacles
        for (int[] obstacle : obstacles){
            if ((obstacle[0] < nextX +10) && (obstacle[2] > nextX -10) && (obstacle[1] < nextY +10) && (obstacle[3] > nextY -10)) {
                return true;
            }
        }
        // Additional check for door collision if door is closed
        if (!isDoorOpen)
            if ((door[0] < nextX +10) && (door[2] > nextX -10) && (door[1] < nextY +10) && (door[3] > nextY -10)) {return true;}
        return false;
    }

    public void movePlayer(char direction) {
        player.velocityY += stage.getGravity(); // Apply gravity to vertical velocity

        // Horizontal movement
        if (direction == 'R') {
            if (!checkCollision(player.getX() + stage.getVelocityX(), player.getY(), obstacles))
                player.setX(player.getX() + stage.getVelocityX());
        }
        if (direction == 'L') {
            if (!checkCollision(player.getX() - stage.getVelocityX(), player.getY(), obstacles))
                player.setX(player.getX() - stage.getVelocityX());
        }

        // Special behavior for stage 2 - auto-jump when on ground
        if (getState().getStageNumber() == 2){
            // Auto-jump when on ground
            boolean isOnGround = false;
            // Check if there's an obstacle under the player's feet
            for (int[] obstacle : obstacles) {
                if ((obstacle[0] < player.getX() + 10) &&
                        (obstacle[2] > player.getX() - 10) &&
                        (obstacle[1] <= player.getY() - 10) &&
                        (obstacle[3] >= player.getY() - 11)) {
                    isOnGround = true;
                    break;
                }
            }
            if (isOnGround) {
                player.velocityY = stage.getVelocityY(); // Auto-jump

            }
        }

        // Jump control - only when on ground
        if ((direction == 'U')) {
            // Check if player is on ground
            boolean isOnGround = checkCollision(player.getX(), player.getY() - 1, obstacles);

            if (isOnGround && player.velocityY <= 0) {  // If on ground and not falling
                player.velocityY = stage.getVelocityY();  // Apply upward velocity
            }
        }

        // Apply vertical movement
        double nextY = player.getY() + player.velocityY;

        if (!checkCollision(player.getX(), nextY, obstacles)) {
            // No collision - proceed to new Y position
            player.setY(nextY);
        } else {
            if (player.velocityY < 0) {
                // Hit something while falling - stop vertical movement
                player.velocityY = 0;

                // Find ground height (move player up until no collision)
                while (checkCollision(player.getX(), player.getY(), obstacles)) {
                    player.setY(player.getY()+0.1);
                }
            } else {
                // Hit ceiling while jumping - stop vertical movement
                player.velocityY = 0;
            }
        }

        // Check if player is on the button
        boolean playerOnButton = (player.getX() + 10 > 400) && (player.getX() - 10 < 470) &&
                (player.getY() + 10 > 390) && (player.getY() - 10 < 410);

        // Button press handling
        //400-470  390-410
        if (playerOnButton && !isOnButton) {
            pressButton();
            isOnButton = true;
        } else if (!playerOnButton && isOnButton) {
            isOnButton = false;
        }

        // Check for collision with spikes - respawn if hit
        for (int[] spike : spikes) {
            if ((player.getX() + 10 >= spike[0]) && (player.getX() -10 < spike[2]) && (player.getY() +10 >= spike[1]) && (player.getY() -10 <= spike[3])) {
                player.respawn(new int[]{110, 430, 150, 450});
                isDoorOpen = false;
                buttonPressNum = 0;
                Game.deathNumber += 1; // Increment death counter in Game class
            }
        }

        // Door opening logic - different requirements based on stage
        if (stage.getStageNumber() == 3) {
            if (buttonPressNum == 5){
                isDoorOpen = true;
                //685 180 700 240
                // Draw open door (white rectangle)
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledRectangle(692.5,210,7.5,30);
            }
        } else {
            if (buttonPressNum == 1){
                isDoorOpen = true;
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledRectangle(692.5,210,7.5,30);
            }
        }
    }

    //Resets the stage to initial state
    //Places player at start position and resets door and button state
    public void restartStage(){
        player.respawn(new int[]{110, 430, 150, 450});
        isDoorOpen = false;
        buttonPressNum = 0;
    }

    //Increments button press counter when button is pressed
    public void pressButton(){
        buttonPressNum += 1;
    }

    public boolean changeStage(){
        // Check if player is at exit pipe location
        //{new int[]{720, 175, 740, 215},
        //  new int[]{740, 180, 770, 210}}
        if ((player.getX() +10 >= 720) && (player.getY()-10 <= 215) && (player.getY()+10 >= 175)) {
            isDoorOpen = false;
            buttonPressNum = 0;
            return true;
        }
        return false;
    }

    // Random color generator for visual effects
    Random random = new Random();
    int r = random.nextInt(256); // 0-255
    int g = random.nextInt(256);
    int b = random.nextInt(256);

    public void drawPipe(){
        for (int[] startPipeRect : startPipe) {
            double centerXStartPipeRect = (startPipeRect[2] + startPipeRect[0]) / 2.0;
            double centerYStartPipeRect = (startPipeRect[3] + startPipeRect[1]) / 2.0;
            double halfWidthStartPipeRect = (startPipeRect[2] - startPipeRect[0]) / 2.0;
            double halfHeightStartPipeRect = (startPipeRect[3] - startPipeRect[1]) / 2.0;
            StdDraw.setPenColor(255, 215, 0);
            StdDraw.filledRectangle(centerXStartPipeRect, centerYStartPipeRect, halfWidthStartPipeRect, halfHeightStartPipeRect);
        }
    }

    //Renders all game elements on the screen
    public void draw() {

        // Draw all obstacles with random color
        for (int[] obstacle : obstacles) {
                double centerX = (obstacle[2] + obstacle[0]) / 2.0;
                double centerY = (obstacle[3] + obstacle[1]) / 2.0;
                double halfWidth = (obstacle[2] - obstacle[0]) / 2.0;
                double halfHeight = (obstacle[3] - obstacle[1]) / 2.0;
                StdDraw.setPenColor(r,g,b);
                StdDraw.filledRectangle(centerX, centerY, halfWidth, halfHeight);
            }

        // Draw the button in red if not pressed
        if (!isOnButton) {
            double centerXButton = (button[2] + button[0]) / 2.0;
            double centerYButton = (button[3] + button[1]) / 2.0;
            double halfWidthButton = (button[2] - button[0]) / 2.0;
            double halfHeightButton = (button[3] - button[1]) / 2.0;
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.filledRectangle(centerXButton, centerYButton, halfWidthButton, halfHeightButton);
        }

        // Draw the button floor in light gray
        double centerXButtonFloor = (buttonFloor[2] + buttonFloor[0]) / 2.0;
        double centerYButtonFloor = (buttonFloor[3] + buttonFloor[1]) / 2.0;
        double halfWidthButtonFloor = (buttonFloor[2] - buttonFloor[0]) / 2.0;
        double halfHeightButtonFloor = (buttonFloor[3] - buttonFloor[1]) / 2.0;
        StdDraw.setPenColor(200, 200, 200);
        StdDraw.filledRectangle(centerXButtonFloor, centerYButtonFloor, halfWidthButtonFloor, halfHeightButtonFloor);

        // Draw the starting pipe in gold color
        for (int[] startPipeRect : startPipe) {
            double centerXStartPipeRect = (startPipeRect[2] + startPipeRect[0]) / 2.0;
            double centerYStartPipeRect = (startPipeRect[3] + startPipeRect[1]) / 2.0;
            double halfWidthStartPipeRect = (startPipeRect[2] - startPipeRect[0]) / 2.0;
            double halfHeightStartPipeRect = (startPipeRect[3] - startPipeRect[1]) / 2.0;
            StdDraw.setPenColor(255, 215, 0);
            StdDraw.filledRectangle(centerXStartPipeRect, centerYStartPipeRect, halfWidthStartPipeRect, halfHeightStartPipeRect);
        }

        // Draw the exit pipe in gold color
        for (int[] exitPipeRect : exitPipe) {
            double centerXExitPipeRect = (exitPipeRect[2] + exitPipeRect[0]) / 2.0;
            double centerYExitPipeRect = (exitPipeRect[3] + exitPipeRect[1]) / 2.0;
            double halfWidthExitPipeRect = (exitPipeRect[2] - exitPipeRect[0]) / 2.0;
            double halfHeightExitPipeRect = (exitPipeRect[3] - exitPipeRect[1]) / 2.0;
            StdDraw.setPenColor(255, 215, 0);
            StdDraw.filledRectangle(centerXExitPipeRect, centerYExitPipeRect, halfWidthExitPipeRect, halfHeightExitPipeRect);
        }

        // Draw spikes with appropriate orientation based on their position
        for (int i = 0; i < spikes.length; i++) {
            double centerXSpike = (spikes[i][2] + spikes[i][0]) / 2.0;
            double centerYSpike = (spikes[i][3] + spikes[i][1]) / 2.0;
            double widthSpike = (spikes[i][2] - spikes[i][0]);
            double heightSpike = (spikes[i][3] - spikes[i][1]);
            if (i == 0) {
                // Draw left-facing spikes
                StdDraw.picture(centerXSpike + 15, centerYSpike, "misc/SpikesLeft.png", widthSpike * 2.5, heightSpike * 1.4);
            } else if (i == 1 || i == 2 || i == 3) {
                // Draw downward-facing spikes
                StdDraw.picture(centerXSpike, centerYSpike + 15, "misc/SpikesDown.png", widthSpike * 1.6, heightSpike * 2.5);
            } else if (i == 4) {
                // Draw right-facing spikes
                StdDraw.picture(centerXSpike - 14, centerYSpike, "misc/SpikesRight.png", widthSpike * 2.5, heightSpike * 1.4);
            } else {
                // Draw upward-facing spikes
                StdDraw.picture(centerXSpike, centerYSpike - 15, "misc/Spikes.png", widthSpike * 1.6, heightSpike * 2.5);
            }
        }

        // Calculate door dimensions
        double centerXDoor = (door[2] + door[0]) / 2.0;
        double centerYDoor = (door[3] + door[1]) / 2.0;
        double halfWidthDoor = (door[2] - door[0]) / 2.0;
        double halfHeightDoor = (door[3] - door[1]) / 2.0;

        // Draw the door in blue if it's closed
        if (!isDoorOpen) {
            StdDraw.setPenColor(80, 160, 200);
            StdDraw.filledRectangle(centerXDoor, centerYDoor, halfWidthDoor, halfHeightDoor);
        }

    }
}
