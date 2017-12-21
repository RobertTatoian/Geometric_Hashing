package RogueBytes;

public class Model {
    private int modelID;
    private Point[] modelPoints;

    Model(int modelID, Point[] modelPoints) {
        this.modelID = modelID;
        this.modelPoints = modelPoints;
    }

    /**
     * Scales all the points of an initialized model by a certain factor.
     * @param scale The amount to scale by.
     */
    public void scaleCoordinates(float scale) {
        Point[] scaledCoordinates = new Point[modelPoints.length];

        for (int i = 0; i < modelPoints.length; i++) {
            scaledCoordinates[i].setX(modelPoints[i].getX() * scale);
            scaledCoordinates[i].setY(modelPoints[i].getY() * scale);
        }

        modelPoints = scaledCoordinates;
    }

    /**
     * Scales all the points of a model by a certain factor.
     * @param coordinates The list of coordinates to scale.
     * @param scale The amount to scale by.
     * @return The list of scaled coordinates.
     */
    public static Point[] scaleCoordinates(Point[] coordinates, float scale) {
        Point[] scaledCoordinates = new Point[coordinates.length];

        for (int i = 0; i < scaledCoordinates.length; i++) {
            scaledCoordinates[i] = new Point(coordinates[i].getX() * scale, coordinates[i].getY() * scale);
        }

        return scaledCoordinates;
    }

    /**
     * Shifts all the points of a model by adding a shift amount the provided coordinates.
     * @param coordinates The list of coordinates to shift.
     * @param shift The point to shift to by.
     * @return The list of shifted coordinates.
     */
    public static Point[] shiftCoordinates(Point[] coordinates, Point shift) {
        Point[] shiftedCoordinates = new Point[coordinates.length];

        for (int i = 0; i < coordinates.length; i++) {
            shiftedCoordinates[i] = new Point(coordinates[i].getX() - shift.getX(), coordinates[i].getY() - shift.getY());
        }

        return shiftedCoordinates;
    }

    /**
     * Shifts all the points of a model by a provided angle.
     * @param coordinates The list of coordinates to rotate.
     * @param angle The angle to rotate by.
     * @return The list of rotated coordinates.
     */
    public static Point[] rotateCoordinates(Point[] coordinates, float angle) {
        Point[] rotatedCoordinates = new Point[coordinates.length];

        for (int i = 0; i < coordinates.length; i++) {
            rotatedCoordinates[i] = Point.rotatePoint(coordinates[i], angle);
        }

        return rotatedCoordinates;
    }

    public String generateKey(int firstBasisPoint, int secondBasisPoint) {
        return "(M" + Integer.toString(modelID) + ", (" + Integer.toString(firstBasisPoint) + ", " + Integer.toString(secondBasisPoint) + "))";
    }

    public static String generateKey(int currentModel, int firstBasisPoint, int secondBasisPoint) {
        return "(M" + Integer.toString(currentModel) + ", (" + Integer.toString(firstBasisPoint) + ", " + Integer.toString(secondBasisPoint) + "))";
    }

    public int getModelLength() {
        return modelPoints.length;
    }

    public Point[] getModelPoints() {
        return modelPoints;
    }
}
