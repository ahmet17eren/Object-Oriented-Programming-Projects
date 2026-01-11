// name surname: Ahmet Eren Aslan
// student ID: 2023400018
import java.awt.Color;

public class Stage {
    private  int stageNumber;
    private final double gravity;
    private double velocityX;
    private double velocityY;
    private final int rightCode;
    private final int leftCode;
    private final int upCode;
    private final String clue;
    private final String help;
    private Color color;

    //constructor
    Stage (double gravity, double velocityX, double velocityY , int stageNumber, int  rightCode
            , int leftCode , int upCode , String clue , String help) {
        this.stageNumber = stageNumber;
        this.gravity = gravity;
        this.clue = clue;
        this.help = help;
        this.leftCode = leftCode;
        this.rightCode = rightCode;
        this.upCode = upCode;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }


    // get methods for some instance variables
    public int getStageNumber(){
        return stageNumber;
    }
    public double getGravity(){
        return gravity;
    }
    public double getVelocityX(){
        return velocityX;
    }
    public double getVelocityY(){
        return velocityY;
    }
    public String getClue(){
        return clue;
    }
    public String getHelp(){
        return help;
    }
    // getColor is defined but not used
    public Color getColor(){
        return color;
    }
    public int[] getKeyCodes(){
        // Return array containing control key codes for this stage
        return new int[] {rightCode,leftCode,upCode};
    }
}
