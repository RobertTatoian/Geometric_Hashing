package RogueBytes;

import processing.core.PApplet;

public class Main extends PApplet implements VisualizationConstants{

    private int[][] orionImage = {{2262, 2029},{2648, 1743},{2870, 2119},{2862, 2205},{2844, 2289},{3116, 2715},{3420, 2279}};
    private int[][] orionModel = {{381, 381},{924, 573},{690, 1047},{600, 1098},{504, 1134},{249, 1683},{891, 1707}};

    private float[][] scaledModel;
    private float[][] scaledShiftedModel;
    private float[][] ssrModel;

    private float scale = 1;

    private float p2_xDist = 0;
    private float p2_yDist = 0;
    private double distance = 0;

    private float r_x2 = 0;
    private float r_y2 = 0;

    private float[] r_P2 = {0, 0};

    private float angle = 0;

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
        scale(100,-100);
        strokeWeight(0.01f);
        line(-WORLD_ORIGIN_X,0,WORLD_ORIGIN_X,0);
        line(0,-WORLD_ORIGIN_Y,0,WORLD_ORIGIN_Y);
        strokeWeight(0.05f);
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

    public void draw() {
        setupVisualization();

        //Calculate the distance between two points and determine the value to scale it to 1.
        distance = distance(orionModel[0][0], orionModel[1][0], orionModel[0][1], orionModel[1][1]);
        scale = (float)(1/distance);

        //Scale all the points such that the distance between the two points calculated above is 1.
        scaledModel = scaleCoordinates(orionModel, scale);

        //Shift all the points such that the current point we are working on is at (0, 0).
        scaledShiftedModel = shiftCoordinates(scaledModel, -scaledModel[0][0], -scaledModel[0][1]);

        // Recalculate the scaled distance
        distance = distance(scaledShiftedModel[0][0], scaledShiftedModel[2][0],scaledShiftedModel[0][1], scaledShiftedModel[2][1]);

        // Calulate the angle between the second point and the x-axis.
        angle = (float) calculateAngle(scaledShiftedModel[1][0], (float) distance);
        ssrModel = rotateCoordinates(scaledShiftedModel, angle);

        point(scaledShiftedModel[0][0],scaledShiftedModel[0][1]);
        point(scaledShiftedModel[1][0],scaledShiftedModel[1][1]);
        point(scaledShiftedModel[2][0],scaledShiftedModel[2][1]);
        point(scaledShiftedModel[3][0],scaledShiftedModel[3][1]);
        point(scaledShiftedModel[4][0],scaledShiftedModel[4][1]);
        point(scaledShiftedModel[5][0],scaledShiftedModel[5][1]);
        point(scaledShiftedModel[6][0],scaledShiftedModel[6][1]);

        stroke(204, 102, 0);
        point(ssrModel[0][0], ssrModel[0][1]);
        point(ssrModel[1][0], ssrModel[1][1]);
        point(ssrModel[2][0], ssrModel[2][1]);
        point(ssrModel[3][0], ssrModel[3][1]);
        point(ssrModel[4][0], ssrModel[4][1]);
        point(ssrModel[5][0], ssrModel[5][1]);
        point(ssrModel[6][0], ssrModel[6][1]);
        stroke(0, 0, 0);
    }



    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
