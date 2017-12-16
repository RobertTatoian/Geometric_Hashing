package RogueBytes;

import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends PApplet implements VisualizationConstants{

    private int[][] orionImage = {{2262, 2029},{2648, 1743},{2870, 2119},{2862, 2205},{2844, 2289},{3116, 2715},{3420, 2279}};
    private int[][][] model = {
            // Orion
            {{381, 381}, {924, 573}, {690, 1047}, {600, 1098}, {504, 1134}, {249, 1683}, {891, 1707}},
            // Auriga
            {{119,181}, {149,231}, {228,255}, {244,201}, {189,145}, {163,144}}
    };

    private Point[][] model_Points = {
            {new Point(381, 381), new Point(924, 573), new Point(690, 1047), new Point(600, 1098),
                    new Point(504, 1134) ,new Point(249, 1683) ,new Point(891, 1707)},

            {new Point(119,181), new Point(149,231), new Point(228,255), new Point(244,201),
                    new Point(189,145) ,new Point(163,144)}
    };

    private Model[] m = {
            new Model(0, new Point[] {new Point(381, 381), new Point(924, 573), new Point(690, 1047), new Point(600, 1098),
                    new Point(504, 1134) ,new Point(249, 1683) ,new Point(891, 1707)}),

            new Model(1, new Point[] {new Point(119,181), new Point(149,231), new Point(228,255), new Point(244,201),
                    new Point(189,145) ,new Point(163,144)})
    };

    Map<String, List<int[]>> possibleModels = new HashMap<String, List<int[]>>();

    private int[][] workingModel;
    private float[][] scaledModel;
    private float[][] scaledShiftedModel;
    private float[][] ssrModel;

    Point[] sP;
    Point[] ssP;
    Point[] ssrP;

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
     *  Calculates the distance between two points.
     *
     *  @param x1 The x coordinate of the first point.
     *  @param x2 The x coordinate of the second point.
     *  @param y1 The y coordinate of the first point.
     *  @param y2 The y coordinate of the second point.
     *  @return The distance between the two points.
     */
    private double distance(float x1, float x2, float y1, float y2){
        float a = ((x2 - x1) * (x2 - x1));
        float b = ((y2 - y1) * (y2 - y1));
        float sum = a + b;
        return Math.sqrt(sum);
    }

    /**
     * Scales all the points of a model by a certain factor.
     * @param coordinates The list of coordinates to scale.
     * @param scale The amount to scale by.
     * @return The list of scaled coordinates.
     */
    private float[][] scaleCoordinates(int[][] coordinates, float scale) {
        float[][] scaledCoordinates = new float[coordinates.length][coordinates[0].length];

        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[0].length; j++) {
                scaledCoordinates[i][j] = coordinates[i][j] * scale;
            }
        }

        return scaledCoordinates;
    }

    /**
     * Shifts all the points of a model by adding a shift amount the provided coordinates.
     * @param coordinates The list of coordinates to shift.
     * @param xShift The amount to shift by in the x axis.
     * @param yShift The amount to shift by in the y axis.
     * @return The list of shifted coordinates.
     */
    private float[][] shiftCoordinates(float[][] coordinates, float xShift, float yShift) {
        float[][] shiftedCoordinates = new float[coordinates.length][coordinates[0].length];

        for (int i = 0; i < coordinates.length; i++) {
            shiftedCoordinates[i][0] = coordinates[i][0] + xShift;
            shiftedCoordinates[i][1] = coordinates[i][1] + yShift;
        }

        return shiftedCoordinates;
    }

    /**
     * Shifts all the points of a model by a provided angle.
     * @param coordinates The list of coordinates to rotate.
     * @param angle The angle to rotate by.
     * @return The list of rotated coordinates.
     */
    private float[][] rotateCoordinates(float[][] coordinates, float angle) {
        float[][] rotatedCoordinates = new float[coordinates.length][coordinates[0].length];

        for (int i = 0; i < coordinates.length; i++) {
            rotatedCoordinates[i] = rotatePoint(coordinates[i][0], coordinates[i][1], angle);
        }

        return rotatedCoordinates;
    }

    /**
     * Calculates the rotation of a point about the origin.
     *
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param angle The angle to rotate by.
     * @return An array containing the new coordinates of the point.
     */
    private float[] rotatePoint(float x, float y, float angle) {
        float[] rotatedCoordinates = {0, 0};

        rotatedCoordinates[0] = x * cos(angle) + y * sin(angle);
        rotatedCoordinates[1] = -(x * sin(angle)) + y * cos(angle);

        return rotatedCoordinates;
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


    private void drawModelPoints(float[][] coordinates) {

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
            point(coordinates[j][0], coordinates[j][1]);
        }

        stroke(0, 0, 0);
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
//        background(100f);

        setupVisualization();

        workingModel = model[currentModel];

//        if (shift != i) {
//            //Calculate the distance between two points and determine the value to scale it to 1.
//            distance = distance(workingModel[i][0], workingModel[shift][0], workingModel[i][1], workingModel[shift][1]);
//            scale = (float) (1 / distance);
//
//            //Scale all the points such that the distance between the two points calculated above is 1.
//            scaledModel = scaleCoordinates(workingModel, scale);
//
//            //Shift all the points such that the current point we are working on is at (0, 0).
//            scaledShiftedModel = shiftCoordinates(scaledModel, -scaledModel[i][0], -scaledModel[i][1]);
//
//            // Recalculate the scaled distance
//            distance = distance(scaledShiftedModel[i][0], scaledShiftedModel[shift][0], scaledShiftedModel[i][1], scaledShiftedModel[shift][1]);
//
//            // Calulate the angle between the second point and the x-axis.
//            angle = (float) calculateAngle(scaledShiftedModel[shift][0], (float) distance);
//
//            // Rotate the model by the calculated angle.
//            ssrModel = rotateCoordinates(scaledShiftedModel, angle);
//
//            //TODO Implement hash table storage
//
//            println(generateKey(currentModel, i, shift));
//
//        }

        Point[] working = m[currentModel].getModelPoints();

        if (shift != i) {
            //Calculate the distance between two points and determine the value to scale it to 1.
            distance = Point.distance(working[i],working[shift]);
            scale = (float) (1 / distance);

            sP = Model.scaleCoordinates(working, scale);

            ssP = Model.shiftCoordinates(sP, working[i]);

            distance = Point.distance(ssP[i],ssP[shift]);

            angle = (float) calculateAngle((float) ssP[shift].getX(), (float) distance);

            ssrP = Model.rotateCoordinates(ssP, angle);
        }

        drawModelPoints(ssrP);
        drawModelPoints(ssrModel);

        if (i == workingModel.length - 1) {
            i = 0;
             currentModel = ((currentModel + 1) % model.length);
        } else if (shift == workingModel.length - 1){
            i++;
        }

        shift = ((shift + 1) % workingModel.length);

        delay(1000);
    }



    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
