package RogueBytes;

import processing.core.PApplet;

public class Main extends PApplet implements VisualizationConstants{

    private int[][] orionImage = {{2262, 2029},{2648, 1743},{2870, 2119},{2862, 2205},{2844, 2289},{3116, 2715},{3420, 2279}};

    private Model[] m = {
            new Model(0, new Point[] {new Point(381, 381), new Point(924, 573), new Point(690, 1047), new Point(600, 1098),
                    new Point(504, 1134) ,new Point(249, 1683) ,new Point(891, 1707)}),

            new Model(1, new Point[] {new Point(119,181), new Point(149,231), new Point(228,255), new Point(244,201),
                    new Point(189,145) ,new Point(163,144)})
    };

    private GHasher hasher;

    public void settings() {
        size(WIN_WIDTH, WIN_HEIGHT);
    }

    public void setup() {
        hasher = new GHasher(m);
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
    }

    private void encodeModels() {
        Point[] returnedModel;
        for (int currentModel = 0; currentModel < hasher.numberOfModels(); currentModel++) {
            for (int basisPoint1 = 0; basisPoint1 < hasher.currentModelLength(); basisPoint1++) {
                for (int basisPoint2 = 0; basisPoint2 < hasher.currentModelLength(); basisPoint2++) {
                    if (basisPoint1 != basisPoint2){
                        returnedModel = hasher.calculateForBasisPoints(currentModel, basisPoint1, basisPoint2);
                        drawModelPoints(returnedModel);
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("RogueBytes.Main");
    }
}
