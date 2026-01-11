import java.awt.Color;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.awt.event.KeyEvent;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Canvas properties, scale and set the canvas with the given parameters
        double xScale = 800.0, yScale = 400.0;
        StdDraw.setCanvasSize(800, 400);
        StdDraw.setXscale(0.0, xScale);
        StdDraw.setYscale(0.0, yScale);
        // Color array for bricks (first import java.awt.Color )
        Color[] colors = {new Color(255, 0, 0), new Color(220, 20, 60),
                new Color(178, 34, 34), new Color(139, 0, 0),
                new Color(255, 69, 0), new Color(165, 42, 42)
        };
        // Game Components (These can be changed for custom scenarios),
        double ballRadius = 8; // Ball radius
        double ballVelocity = 4;
        double ballVelocityX = 0;
        double ballVelocityY = 0;    // Magnitude of the ball velocity
        Color ballColor = new Color(15, 82, 186); // Color of the ball
        double[] initialBallPos = {400, 18}; //Initial position of the ball in the format {x, y}
        double[] paddlePos = {400, 5}; // Initial position of the center of the paddle
        double paddleHalfwidth = 60; // Paddle halfwidth
        double paddleHalfheight = 5; // Paddle halfheight
        double paddleSpeed = 15; // Paddle speed
        Color paddleColor = new Color(128, 128, 128); // Paddle color
        double brickHalfwidth = 50; // Brick halfwidth
        double brickHalfheight = 10; // Brick halfheight
        int score = 0;
        double rad = 0;
        boolean toggle = false;
        boolean spacePressed = true;


        StdDraw.enableDoubleBuffering();

        // 2D array to store center coordinates of bricks in the format {x, y}
        double[][] brickCoordinates = new double[][]{
                {250, 320}, {350, 320}, {450, 320}, {550, 320},
                {150, 300}, {250, 300}, {350, 300}, {450, 300}, {550, 300}, {650, 300},
                {50, 280}, {150, 280}, {250, 280}, {350, 280}, {450, 280}, {550, 280}, {650, 280}, {750, 280},
                {50, 260}, {150, 260}, {250, 260}, {350, 260}, {450, 260}, {550, 260}, {650, 260}, {750, 260},
                {50, 240}, {150, 240}, {250, 240}, {350, 240}, {450, 240}, {550, 240}, {650, 240}, {750, 240},
                {150, 220}, {250, 220}, {350, 220}, {450, 220}, {550, 220}, {650, 220},
                {250, 200}, {350, 200}, {450, 200}, {550, 200}};
        // Brick colors
        Color[] brickColors = new Color[]{colors[0],
                colors[0], colors[1], colors[2], colors[3],
                colors[2], colors[4], colors[3], colors[0], colors[4], colors[5],
                colors[5], colors[0], colors[1], colors[5], colors[2], colors[3], colors[0], colors[4],
                colors[1], colors[3], colors[2], colors[4], colors[0], colors[5], colors[2], colors[1],
                colors[4], colors[0], colors[5], colors[1], colors[2], colors[3], colors[0], colors[5],
                colors[1], colors[4], colors[0], colors[5], colors[1], colors[2],
                colors[3], colors[2], colors[3], colors[0]};


        // Wait until the SPACE key is pressed to start the game
        while (!StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            // Clear the screen
            StdDraw.clear();

            // Draw colorful bricks
            for (int i = 0; i < brickCoordinates.length; i++) {
                StdDraw.setPenColor(brickColors[i]);  // Set the color for each brick
                StdDraw.filledRectangle(brickCoordinates[i][0], brickCoordinates[i][1], brickHalfwidth, brickHalfheight);
            }

            // Draw the paddle
            StdDraw.setPenColor(paddleColor);
            StdDraw.filledRectangle(paddlePos[0], paddlePos[1], paddleHalfwidth, paddleHalfheight);

            // Draw the ball
            StdDraw.setPenColor(ballColor);
            StdDraw.filledCircle(initialBallPos[0], initialBallPos[1], ballRadius);

            // Draw a line indicating the ball's direction
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.setPenRadius(0.005);
            double xLineEnd = initialBallPos[0] + 100 * Math.cos(rad);
            double yLineEnd = initialBallPos[1] + 100 * Math.sin(rad);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(initialBallPos[0], initialBallPos[1], xLineEnd, yLineEnd);
            String formatttedAngle = String.format("%.1f" ,(rad / Math.PI * 180));
            StdDraw.textLeft(20, 380, "Angle: " + formatttedAngle);

            // Adjust the angle of the ball's direction when LEFT or RIGHT arrow keys are pressed
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                if (rad <= Math.PI - 0.017) {  //set left limit
                    rad = rad + 0.017;
                } else {
                    rad = Math.PI;
                }
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                if (rad >= 0.017) {   //set right limit
                    rad = rad - 0.017;
                } else {
                    rad = 0;
                }
            }

            // Display the score
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.textLeft(700, 380, "Score: " + score);

            // Show the frame and pause for a short time
            StdDraw.show();
            StdDraw.pause(10);
        }

// Main game loop
        while (true) {
            // Draw the bricks, paddle, and ball
            for (int i = 0; i < brickCoordinates.length; i++) {
                StdDraw.setPenColor(brickColors[i]);  // Set the color for each brick
                StdDraw.filledRectangle(brickCoordinates[i][0], brickCoordinates[i][1], brickHalfwidth, brickHalfheight);
            }
            StdDraw.setPenColor(paddleColor);
            StdDraw.filledRectangle(paddlePos[0], paddlePos[1], paddleHalfwidth, paddleHalfheight);
            StdDraw.setPenColor(ballColor);
            StdDraw.filledCircle(initialBallPos[0], initialBallPos[1], ballRadius);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.textLeft(700, 380, "Score: " + score);

            // Toggle pause state when SPACE key is pressed
            if (StdDraw.isKeyPressed(32)) {
                if (!spacePressed) {
                    toggle = !toggle;
                    spacePressed = true;
                }
            } else {
                spacePressed = false;
            }

            // If the game is paused, display "PAUSED" and skip the rest of the loop
            if (toggle) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new java.awt.Font("Montserrat", java.awt.Font.BOLD, 24));
                StdDraw.textLeft(100, 350, "PAUSED");
                StdDraw.show();
                continue;
            }

            // Check if the player has won (all bricks are destroyed)
            if (score == brickCoordinates.length * 10) {
                StdDraw.setFont(new java.awt.Font("Montserrat", java.awt.Font.BOLD, 28));
                StdDraw.textLeft(350, 220, "VICTORY");
                StdDraw.show();
                break;
            }

            // Move the paddle left or right based on key input
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                paddlePos[0] -= paddleSpeed;
                if (paddlePos[0] - paddleHalfwidth <= 0) {
                    paddlePos[0] = 0 + paddleHalfwidth;
                }
                StdDraw.setPenColor(paddleColor);
                StdDraw.filledRectangle(paddlePos[0], paddlePos[1], paddleHalfwidth, paddleHalfheight);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                paddlePos[0] += paddleSpeed;
                if (paddlePos[0] + paddleHalfwidth >= 800) {
                    paddlePos[0] = 800 - paddleHalfwidth;
                }
                StdDraw.setPenColor(paddleColor);
                StdDraw.filledRectangle(paddlePos[0], paddlePos[1], paddleHalfwidth, paddleHalfheight);
            }

            // Update the ball's position based on its velocity at the beginning of the game
            StdDraw.setPenColor(ballColor);
            if ((ballVelocityX == 0) && (ballVelocityY == 0)) {
                ballVelocityX = ballVelocity * Math.cos(rad);
                ballVelocityY = ballVelocity * Math.sin(rad);
            }

            // Handle ball collisions with the walls
            if ((initialBallPos[0] + (ballVelocityX) - ballRadius < 0) || (initialBallPos[0] + (ballVelocityX) + ballRadius > xScale)) {
                ballVelocityX = -ballVelocityX;
                initialBallPos[0] += ballVelocityX;
            } else {
                initialBallPos[0] += ballVelocityX;
            }
            if ((initialBallPos[1] + ballVelocityY + ballRadius > yScale)) {
                ballVelocityY = -ballVelocityY;
                initialBallPos[1] += ballVelocityY;
            } else {
                initialBallPos[1] += ballVelocityY;
            }

            // Handle ball collision with the paddle
            if (initialBallPos[0] + ballVelocityX <= paddlePos[0] + paddleHalfwidth && paddlePos[0] - paddleHalfwidth <= initialBallPos[0] + ballVelocityX && initialBallPos[1] + ballVelocityY - ballRadius <= paddlePos[1] + paddleHalfheight) {
                if (ballVelocityX != 0 && ballVelocityY != 0) { //check for initial position of ball
                    ballVelocityY = -ballVelocityY;
                }
            } else if (Math.pow(initialBallPos[0] - paddlePos[0] - paddleHalfwidth,2) + Math.pow(initialBallPos[1]  - paddlePos[1] - paddleHalfheight,2) <= ballRadius*ballRadius){ //right collision
                //dot product corner collision same
                double paddleNormalX = initialBallPos[0] - (paddlePos[0] + paddleHalfwidth);
                double paddleNormalY = initialBallPos[1] - (paddlePos[1] + paddleHalfheight);
                double normalLength = Math.sqrt(paddleNormalX * paddleNormalX + paddleNormalY * paddleNormalY);
                if (normalLength != 0) {
                    paddleNormalX /= normalLength;
                    paddleNormalY /= normalLength;
                }

                // Reflect the ball's velocity based on the normal vector
                double dotProduct = ballVelocityX * paddleNormalX + ballVelocityY * paddleNormalY;
                ballVelocityX = ballVelocityX - 2 * dotProduct * paddleNormalX;
                ballVelocityY = ballVelocityY - 2 * dotProduct * paddleNormalY;

            } else if (Math.pow(initialBallPos[0]  - paddlePos[0] + paddleHalfwidth,2) + Math.pow(initialBallPos[1]  - paddlePos[1] - paddleHalfheight,2) <= ballRadius*ballRadius){ // left collision
                //dot product corner collision same
                double paddleNormalX = initialBallPos[0] - (paddlePos[0] - paddleHalfwidth);
                double paddleNormalY = initialBallPos[1] - (paddlePos[1] + paddleHalfheight);
                double normalLength = Math.sqrt(paddleNormalX * paddleNormalX + paddleNormalY * paddleNormalY);
                if (normalLength != 0) {
                    paddleNormalX /= normalLength;
                    paddleNormalY /= normalLength;
                }

                // Reflect the ball's velocity based on the normal vector
                double dotProduct = ballVelocityX * paddleNormalX + ballVelocityY * paddleNormalY;
                ballVelocityX = ballVelocityX - 2 * dotProduct * paddleNormalX;
                ballVelocityY = ballVelocityY - 2 * dotProduct * paddleNormalY;
            }

            // Check if the ball falls below the paddle (game over)
            if (initialBallPos[1] + (ballVelocityY) - ballRadius < 0) {
                StdDraw.setFont(new java.awt.Font("Montserrat", java.awt.Font.BOLD, 24));
                StdDraw.textLeft(335, 150, "GAME OVER");
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
                StdDraw.textLeft(360, 130, "Score: " + score);
                StdDraw.filledCircle(initialBallPos[0], initialBallPos[1], ballRadius);
                StdDraw.show();
                break;
            }

            // Check for collisions between the ball and bricks
            for (int num = 0; num < brickCoordinates.length; num++) {
                double left = brickCoordinates[num][0] - brickHalfwidth;
                double right = brickCoordinates[num][0] + brickHalfwidth;
                double top = brickCoordinates[num][1] + brickHalfheight;
                double bottom = brickCoordinates[num][1] - brickHalfheight;

                // Check for collisions on the top, bottom, left, or right sides of the brick
                boolean topCollision = initialBallPos[1] + ballVelocityY + ballRadius > top && initialBallPos[1] + ballVelocityY < top && initialBallPos[0] + ballVelocityX > left && initialBallPos[0] + ballVelocityX < right;
                boolean bottomCollision = initialBallPos[1] + ballVelocityY - ballRadius < bottom && initialBallPos[1] + ballVelocityY > bottom && initialBallPos[0] + ballVelocityX > left && initialBallPos[0] + ballVelocityX < right;
                boolean leftCollision = initialBallPos[0] + ballVelocityX + ballRadius > left && initialBallPos[0] + ballVelocityX < left && initialBallPos[1] + ballVelocityY > bottom && initialBallPos[1] + ballVelocityY < top;
                boolean rightCollision = initialBallPos[0] + ballVelocityX - ballRadius < right && initialBallPos[0] + ballVelocityX > right && initialBallPos[1] + ballVelocityY > bottom && initialBallPos[1] + ballVelocityY < top;

                // Check for corner collisions
                boolean cornerCollision = isCornerCollision(left, top, initialBallPos, ballVelocityX, ballVelocityY, ballRadius) ||
                        isCornerCollision(right, top, initialBallPos, ballVelocityX, ballVelocityY, ballRadius) ||
                        isCornerCollision(left, bottom, initialBallPos, ballVelocityX, ballVelocityY, ballRadius) ||
                        isCornerCollision(right, bottom, initialBallPos, ballVelocityX, ballVelocityY, ballRadius);

                // Handle collisions
                if (topCollision || bottomCollision || leftCollision || rightCollision || cornerCollision) {
                    if (cornerCollision) {
                        // Handle corner collisions by reflecting the ball's velocity
                        double topLeftX = brickCoordinates[num][0] - brickHalfwidth;
                        double topLeftY = brickCoordinates[num][1] + brickHalfheight;
                        double topRightX = brickCoordinates[num][0] + brickHalfwidth;
                        double topRightY = brickCoordinates[num][1] + brickHalfheight;
                        double bottomLeftX = brickCoordinates[num][0] - brickHalfwidth;
                        double bottomLeftY = brickCoordinates[num][1] - brickHalfheight;
                        double bottomRightX = brickCoordinates[num][0] + brickHalfwidth;
                        double bottomRightY = brickCoordinates[num][1] - brickHalfheight;

                        // Calculate distances to each corner
                        double topLeftDist = Math.pow(initialBallPos[0] - topLeftX, 2) + Math.pow(initialBallPos[1] - topLeftY, 2);
                        double topRightDist = Math.pow(initialBallPos[0] - topRightX, 2) + Math.pow(initialBallPos[1] - topRightY, 2);
                        double bottomLeftDist = Math.pow(initialBallPos[0] - bottomLeftX, 2) + Math.pow(initialBallPos[1] - bottomLeftY, 2);
                        double bottomRightDist = Math.pow(initialBallPos[0] - bottomRightX, 2) + Math.pow(initialBallPos[1] - bottomRightY, 2);

                        // Find the closest corner
                        double minDist = Math.min(Math.min(topLeftDist, topRightDist), Math.min(bottomLeftDist, bottomRightDist));
                        double cornerX = 0;
                        double cornerY = 0;
                        if (minDist == topLeftDist) {
                            cornerX = topLeftX;
                            cornerY = topLeftY;
                        }
                        if (minDist == topRightDist) {
                            cornerX = topRightX;
                            cornerY = topRightY;
                        }
                        if (minDist == bottomLeftDist) {
                            cornerX = bottomLeftX;
                            cornerY = bottomLeftY;
                        }
                        if (minDist == bottomRightDist) {
                            cornerX = bottomRightX;
                            cornerY = bottomRightY;
                        }

                        // Calculate the normal vector for the collision
                        double normalX = initialBallPos[0] - cornerX;
                        double normalY = initialBallPos[1] - cornerY;
                        double normalLength = Math.sqrt(normalX * normalX + normalY * normalY);
                        if (normalLength != 0) {
                            normalX /= normalLength;
                            normalY /= normalLength;
                        }

                        // Reflect the ball's velocity based on the normal vector
                        double dotProduct = ballVelocityX * normalX + ballVelocityY * normalY;
                        ballVelocityX = ballVelocityX - 2 * dotProduct * normalX;
                        ballVelocityY = ballVelocityY - 2 * dotProduct * normalY;
                    } else {
                        // Handle side collisions by flipping the ball's velocity
                        if (topCollision || bottomCollision) {
                            ballVelocityY = -ballVelocityY;
                        } else  {
                            ballVelocityX = -ballVelocityX;
                        }
                    }

                    // Remove the brick and update the score
                    StdDraw.setPenColor(0, 0, 0);
                    StdDraw.filledRectangle(brickCoordinates[num][0], brickCoordinates[num][1], brickHalfwidth, brickHalfheight);
                    brickCoordinates[num] = new double[]{-100, -100};
                    score += 10;
                }
            }

            // Draw the ball and update the display
            StdDraw.show();
            StdDraw.pause(10);
            StdDraw.clear();
        }
    }
        // Helper method to check for corner collisions
        public static boolean isCornerCollision(double cornerX, double cornerY, double[] ballPos, double ballVelocityX, double ballVelocityY, double ballRadius) {
            double nextBallX = ballPos[0] + ballVelocityX;
            double nextBallY = ballPos[1] + ballVelocityY;
            double distanceSquared = Math.pow(nextBallX - cornerX, 2) + Math.pow(nextBallY - cornerY, 2);
            return distanceSquared <= Math.pow(ballRadius, 2);
        }
    }