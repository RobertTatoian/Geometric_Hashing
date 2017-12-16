package RogueBytes;

import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends PApplet implements VisualizationConstants{

    private int[][] orionImage = {{2262, 2029},{2648, 1743},{2870, 2119},{2862, 2205},{2844, 2289},{3116, 2715},{3420, 2279}};

    private Model[] m = {
            new Model(0, new Point[] {new Point(381, 381), new Point(924, 573), new Point(690, 1047), new Point(600, 1098),
                    new Point(504, 1134) ,new Point(249, 1683) ,new Point(891, 1707)}),

            new Model(1, new Point[] {new Point(119,181), new Point(149,231), new Point(228,255), new Point(244,201),
                    new Point(189,145) ,new Point(163,144)})
    };

    Map<String, List<int[]>> possibleModels = new HashMap<String, List<int[]>>();

    private Point[] workingModel;
    private Point[] scaledModel;
    private Point[] scaledShiftedModel;
    private Point[] ssrModel;

    private float scale = 1;
    private double distance = 0;
    private float angle = 0;

    int i = 0;
    int shift = 1;
    int currentModel = 0;

    public void settings() {
        size(WIN_WIDTH, WIN_HEIGHT);
    }

    public void setup() {

    }

    /**
     * Draws the axes of the visualization.
     */
    private void setupVisualization() {
        // Processing stuff to visualize our world.
        translate(WORLD_ORIGIN_X, WORLD_ORIGIN_Y);
        scale(75,-75);
        strokeWeight(0.01f);
        line(-WORLD_ORIGIN_X,0,WORLD_ORIGIN_X,0);
        line(0,-WORLD_ORIGIN_Y,0,WORLD_ORIGIN_Y);
        strokeWeight(0.04f);
    }

    /**
     * Calculates the angle between the adjacent and hypotenuse.
     * @param adjacent The length of the adjacent side of the triangle.
     * @param hypotenuse The length of the hypotenuse of the triangle.
     * @return The angle in radians.
     */
    private double calculateAngle(float adjacent, float hypotenuse){

        return Math.acos(adjacent/hypotenuse);
    }

    private void drawModelPoints(Point[] coordinates) {

        int numberOfPoints = coordinates.length;

        for (int j = 0; j < numberOfPoints; j++) {
            // For the life of me I can't remember how to change the color depending on with iteration
            // the loop is on and this part is not important in the slightest, so...
            switch (j){
                case 0:
                    stroke(255, 0, 0);
                    break;
                case 1:
                    stroke(0, 255, 0);
                    break;
                case 2:
                    stroke(0, 0, 255);
                    break;
                case 3:
                    stroke(255, 0, 255);
                    break;
                case 4:
                    stroke(255, 255, 0);
                    break;
                case 5:
                    stroke(0, 255, 255);
                    break;
                case 6:
                    stroke(255, 255, 255);
                    break;
                default:
                    stroke(0);
                    break;
            }
            point((float) coordinates[j].getX(), (float) coordinates[j].getY());
        }

        stroke(0, 0, 0);
    }

    private String generateKey(int currentModel, int firstBasisPoint, int secondBasisPoint) {
        return "(M" + Integer.toString(currentModel) + ", (" + Integer.toString(firstBasisPoint) + ", " + Integer.toString(secondBasisPoint) + "))";
    }

    public void draw() {
        background(100f);
        String model = Model.generateKey(currentModel, i, shift);
        text(model, 0, 100);
        setupVisualization();

        workingModel = m[currentModel].getModelPoints();

        if (shift != i) {
            //Calculate the distance between two points and determine the value to scale it to 1.
            distance = Point.distance(workingModel[i],workingModel[shift]);
            scale = (float) (1 / distance);

            //Scale all the points such that the distance between the two points calculated above is 1.
            scaledModel = Model.scaleCoordinates(workingModel, scale);

            //Shift all the points such that the current point we are working on is at (0, 0).
            scaledShiftedModel = Model.shiftCoordinates(scaledModel, scaledModel[i]);

            // Recalculate the scaled distance
            distance = Point.distance(scaledShiftedModel[i],scaledShiftedModel[shift]);
            println("The distance for model: " + model + " is: " + (float) distance);
            println("The adjacent side for our model: " + model + " is: " + (float) scaledShiftedModel[shift].getX());

            // Calulate the angle between the second point and the x-axis.
            angle = (float) calculateAngle((float) scaledShiftedModel[shift].getX(), (float) distance);
            println("The angle calculated to rotate our model: " + model + " is: " + angle);

            // If the Y of the point in below the x axis, invert the angle.
            if (scaledShiftedModel[shift].getY() < 0) {
                angle = -angle;
            }
            
            // Rotate the model by the calculated angle.
            ssrModel = Model.rotateCoordinates(scaledShiftedModel, angle);

            //TODO Implement hash table storage

//            println(Model.generateKey(currentModel, i, shift));
        }

        drawModelPoints(ssrModel);


    }

    public void mouseClicked() {
        if (i == workingModel.length - 1) {
            i = 0;
            currentModel = ((currentModel + 1) % m[currentModel].getModelPoints().length);
        } else if (shift == workingModel.length - 1){
            i++;
        }

        shift = ((shift + 1) % workingModel.length);
    }


    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
