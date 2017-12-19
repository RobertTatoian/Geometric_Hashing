package RogueBytes;

import processing.core.PApplet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    Map<String, List<String>> possibleModels = new HashMap<>();

    private Point[] workingModel;
    private Point[] scaledModel;
    private Point[] scaledShiftedModel;
    private Point[] ssrModel;

    private float scale = 1;
    private double distance = 0;
    private float angle = 0;

    private int shiftedPoint = 1;

    public void settings() {
        size(WIN_WIDTH, WIN_HEIGHT);
    }

    public void setup() {
        noLoop();
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

    private void calculateForBasisPoints(int currentModel, int point1, int point2) {
        String key = Model.generateKey(currentModel, point1, point2);

        workingModel = m[currentModel].getModelPoints();

        //Calculate the distance between two points and determine the value to scale it to 1.
        distance = Point.distance(workingModel[point1], workingModel[point2]);
        scale = (float) (1 / distance);

        //Scale all the points such that the distance between the two points calculated above is 1.
        scaledModel = Model.scaleCoordinates(workingModel, scale);

        //Shift all the points such that the current point we are working on is at (0, 0).
        scaledShiftedModel = Model.shiftCoordinates(scaledModel, scaledModel[point1]);

        // Recalculate the scaled distance
        distance = Point.distance(scaledShiftedModel[point1], scaledShiftedModel[point2]);

        // Calulate the angle between the second point and the x-axis.
        angle = (float) calculateAngle((float) scaledShiftedModel[point2].getX(), (float) distance);

        // If the Y of the point in below the x axis, invert the angle.
        if (scaledShiftedModel[point2].getY() < 0) {
            angle = -angle;
        }

        // Rotate the model by the calculated angle.
        ssrModel = Model.rotateCoordinates(scaledShiftedModel, angle);

        // Encode the positions of the points and the model used to get them.
        for (int j = 0; j < m[currentModel].getModelPoints().length; j++) {
            if (point1 != j) {
                BigDecimal x = new BigDecimal(ssrModel[j].getX());
                BigDecimal y = new BigDecimal(ssrModel[j].getY());
                Point temp = new Point(x.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue(), y.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue());

                List<String> modelUsed = possibleModels.computeIfAbsent(temp.toString(), k -> new ArrayList<String>());
                modelUsed.add(key);
            }
        }
    }

    public void draw() {
        for (int currentModel = 0; currentModel < m.length; currentModel++) {
            for (int currentPoint = 0; currentPoint < m[currentModel].getModelPoints().length; currentPoint++){


                println(key);
                text(key, 0, 100);
                setupVisualization();

                workingModel = m[currentModel].getModelPoints();

                if (shiftedPoint != currentPoint) {
                    //Calculate the distance between two points and determine the value to scale it to 1.
                    distance = Point.distance(workingModel[currentPoint], workingModel[shiftedPoint]);
                    scale = (float) (1 / distance);

                    //Scale all the points such that the distance between the two points calculated above is 1.
                    scaledModel = Model.scaleCoordinates(workingModel, scale);

                    //Shift all the points such that the current point we are working on is at (0, 0).
                    scaledShiftedModel = Model.shiftCoordinates(scaledModel, scaledModel[currentPoint]);

                    // Recalculate the scaled distance
                    distance = Point.distance(scaledShiftedModel[currentPoint], scaledShiftedModel[shiftedPoint]);

                    // Calulate the angle between the second point and the x-axis.
                    angle = (float) calculateAngle((float) scaledShiftedModel[shiftedPoint].getX(), (float) distance);

                    // If the Y of the point in below the x axis, invert the angle.
                    if (scaledShiftedModel[shiftedPoint].getY() < 0) {
                        angle = -angle;
                    }

                    // Rotate the model by the calculated angle.
                    ssrModel = Model.rotateCoordinates(scaledShiftedModel, angle);


                    for (int j = 0; j < m[currentModel].getModelPoints().length; j++) {
                        if (currentPoint != j) {
                            BigDecimal x = new BigDecimal(ssrModel[j].getX());
                            BigDecimal y = new BigDecimal(ssrModel[j].getY());
                            Point temp = new Point(x.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue(), y.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue());

                            List<String> modelUsed = possibleModels.get(temp.toString());
                            if (modelUsed == null) {
                                possibleModels.put(temp.toString(), modelUsed = new ArrayList<String>());
                            }
                            modelUsed.add(key);
                        }
                    }

                }

                drawModelPoints(ssrModel);

                if (currentPoint == workingModel.length - 1 && shiftedPoint == workingModel.length - 1) {
                    currentPoint = 0;
                } else if (shiftedPoint == workingModel.length - 1) {
                    currentPoint++;
                }

                shiftedPoint = ((shiftedPoint + 1) % workingModel.length);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
