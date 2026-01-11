// name surname: Ahmet Eren Aslan
// student ID: 2023400018

import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class AhmetErenAslan {
    public static void main(String[] args){


//        Uncommnent the following for given stages
//
       int nullButton = -1;
//
//        // Given Stages
        Stage s1 = new Stage(-0.45, 3.65,10,0, KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_UP,"Arrow keys are required","Arrow keys move player ,press button and enter the second pipe"); // normal game
        Stage s2 = new Stage(-0.45, 3.65,10,1,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_UP,"Not always straight forward","Right and left buttons reversed"); // Reversed Buttons
        Stage s3 = new Stage(-2, 3.65, 22,2,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,nullButton,"A bit bouncy here","You jump constantly"); // bouncing
        Stage s4 = new Stage(-0.45, 3.65,10,3,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_UP,"Never gonna give you up","Press button 5 times "); //
        Stage s5 = new Stage(-0.45, 3.15,10,4,KeyEvent.VK_RIGHT,nullButton,KeyEvent.VK_UP,"Don't look back","You can't use left button ") ;
     // Add a new stage here
//
//        // Add the stages to the arraylist
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(s1);
        stages.add(s2);
        stages.add(s3);
        stages.add(s4);
        stages.add(s5);

        StdDraw.enableDoubleBuffering(); // Enable double buffering for smoother animation
        Game game = new Game(stages); // Initialize game with configured stages
        game.play();   // Start the game
    }
}
