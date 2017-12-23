package RogueBytes;

import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet implements VisualizationConstants{

    private Model[] images = {
            new Model(0, new Point[] {
                    new Point(2262, 2029), new Point(2648, 1743), new Point(2870, 2119), new Point(2862, 2205),
                    new Point(2844, 2289), new Point(3116, 2715), new Point(3420, 2279)})
    };

    private Model[] models = {
            new Model(0, new Point[] {new Point(381, 381), new Point(924, 573), new Point(690, 1047), new Point(600, 1098),
                    new Point(504, 1134) ,new Point(249, 1683) ,new Point(891, 1707)}),

            new Model(1, new Point[] {new Point(119,181), new Point(149,231), new Point(228,255), new Point(244,201),
                    new Point(189,145) ,new Point(163,144)})
    };

    private Map<String, List<String>> hashedModels = new HashMap<>();
    private Map<String, List<String>> hashedImage = new HashMap<>();

    private Collection<String> foundPoints = new ArrayList<>();
    private String[] points;
    private Map<String, Integer> objectHistogram = new HashMap<>();


    private GHasher modelHasher;
    private GHasher imageHasher;

    public void settings() {
        size(WIN_WIDTH, WIN_HEIGHT);
    }

    public void setup() {
        modelHasher = new GHasher(models);
        imageHasher = new GHasher(images);
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

    public void draw() {

        setupVisualization();
        encodeModels();
        encodeImages();



    }

    private void encodeModels() {
        Point[] returnedModel;
        for (int currentModel = 0; currentModel < modelHasher.numberOfModels(); currentModel++) {
            for (int basisPoint1 = 0; basisPoint1 < modelHasher.currentModelLength(); basisPoint1++) {
                for (int basisPoint2 = 0; basisPoint2 < modelHasher.currentModelLength(); basisPoint2++) {
                    if (basisPoint1 != basisPoint2){
                        returnedModel = modelHasher.calculateForBasisPoints(currentModel, basisPoint1, basisPoint2);
                        drawModelPoints(returnedModel);
                    }
                }

            }
        }

        hashedModels = modelHasher.getPossibleModels();
    }

    private void encodeImages() {
        Point[] returnedModel;
        for (int currentModel = 0; currentModel < imageHasher.numberOfModels(); currentModel++) {
            for (int basisPoint1 = 0; basisPoint1 < imageHasher.currentModelLength(); basisPoint1++) {
                for (int basisPoint2 = 0; basisPoint2 < imageHasher.currentModelLength(); basisPoint2++) {
                    if (basisPoint1 != basisPoint2){
                        returnedModel = imageHasher.calculateForBasisPoints(currentModel, basisPoint1, basisPoint2);
                        drawModelPoints(returnedModel);
                    }
                }

            }
        }

        hashedImage = imageHasher.getPossibleModels();
    }

    private void determineMatches() {
        foundPoints = hashedImage.keySet();
        points = foundPoints.toArray(new String[0]);

        for (int i = 0; i < points.length; i++) {
            List<String> temp = hashedModels.get(points[i]);
            if (temp != null) {
                String[] tempStrings = temp.toArray(new String[0]);
                for (int j = 0; j < tempStrings.length; j++) {
                    Integer integer = objectHistogram.get(tempStrings[j]);
                    if (integer == null) {
                        objectHistogram.put(tempStrings[j], 1);
                    } else {
                        objectHistogram.put(tempStrings[j], integer + 1);
                    }
                }
            }
        }

        objectHistogram.entrySet().removeIf(stringIntegerEntry -> stringIntegerEntry.getValue() < 4);

        objectHistogram.forEach((k,v) -> {
            System.out.println("Possible match in model: " + k);
        });
    }

    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
