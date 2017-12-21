package RogueBytes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GHasher {

    private Model[] models;

    private Map<String, List<String>> possibleModels = new HashMap<>();

    private int currentModel = 0;

    GHasher(Model[] models) {
        this.models = models;
    }

    public Point[] calculateForBasisPoints(int currentModel, int point1, int point2) {

        this.currentModel = currentModel;

        String key = Model.generateKey(currentModel, point1, point2);

        Point[] workingModel = models[currentModel].getModelPoints();

        //Calculate the distance between two points and determine the value to scale it to 1.
        double distance = Point.distance(workingModel[point1], workingModel[point2]);
        float scale = (float) (1 / distance);

        //Scale all the points such that the distance between the two points calculated above is 1.
        Point[] scaledModel = Model.scaleCoordinates(workingModel, scale);

        //Shift all the points such that the current point we are working on is at (0, 0).
        Point[] scaledShiftedModel = Model.shiftCoordinates(scaledModel, scaledModel[point1]);

        // Recalculate the scaled distance
        distance = Point.distance(scaledShiftedModel[point1], scaledShiftedModel[point2]);

        // Calulate the angle between the second point and the x-axis.
        float angle = (float) calculateAngle((float) scaledShiftedModel[point2].getX(), (float) distance);

        // If the Y of the point in below the x axis, invert the angle.
        if (scaledShiftedModel[point2].getY() < 0) {
            angle = -angle;
        }

        // Rotate the model by the calculated angle.
        Point[] ssrModel = Model.rotateCoordinates(scaledShiftedModel, angle);

        // Encode the positions of the points and the model used to get them.
        for (int j = 0; j < models[currentModel].getModelPoints().length; j++) {
            if (point1 != j) {
                BigDecimal x = new BigDecimal(ssrModel[j].getX());
                BigDecimal y = new BigDecimal(ssrModel[j].getY());
                Point temp = new Point(x.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue(), y.setScale(1, RoundingMode.HALF_UP).round(MathContext.UNLIMITED).doubleValue());

                List<String> modelUsed = possibleModels.get(temp.toString());
                if (modelUsed == null)
                    possibleModels.put(temp.toString(), modelUsed = new ArrayList<String>());
                modelUsed.add(key);
            }
        }

        return ssrModel;
    }

    /**
     * Gets the hash table containing all the possible models.
     * @return the models.
     */
    public Map<String, List<String>> getPossibleModels() {
        return possibleModels;
    }

    /**
     * Gets the length of the current model.
     * @return The length of the current model.
     */
    public int currentModelLength() {
        return models[currentModel].getModelLength();
    }

    /**
     * Get the number of models provided to the hasher.
     * @return the number of models.
     */
    public int numberOfModels(){
        return models.length;
    }
    /**
     * Generates a value to associate with a key in the hash table.
     * @param currentModel A number representing the current model
     * @param firstBasisPoint The first point in the basis
     * @param secondBasisPoint The second point in the basis
     * @return A string representing the model and basis points.
     */
    private String generateKey(int currentModel, int firstBasisPoint, int secondBasisPoint) {
        return "(M" + Integer.toString(currentModel) + ", (" + Integer.toString(firstBasisPoint) + ", " + Integer.toString(secondBasisPoint) + "))";
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

}
