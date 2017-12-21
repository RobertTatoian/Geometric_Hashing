package RogueBytes;

public class Point {
    private double x;
    private double y;

    Point() {
        this.x = 0;
        this.y = 0;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  Calculates the distance between two points.
     *  @param p1 The first point.
     *  @param p2 The second point.
     *  @return The distance between the two points.
     */
    public static double distance(Point p1, Point p2){
        double a = ((p2.x - p1.x) * (p2.x - p1.x));
        double b = ((p2.y - p1.y) * (p2.y - p1.y));
        double sum = a + b;
        return Math.sqrt(sum);
    }

    /**
     * Calculates the rotation of a point about the origin.
     * @param point The point to rotate.
     * @param angle The angle to rotate by.
     * @return An array containing the new coordinates of the point.
     */
    public static Point rotatePoint(Point point, float angle) {
        double[] rotatedCoordinates = {0, 0};

        rotatedCoordinates[0] = point.x * Math.cos(angle) + point.y * Math.sin(angle);
        rotatedCoordinates[1] = -(point.x * Math.sin(angle)) + point.y * Math.cos(angle);

        return new Point(rotatedCoordinates[0], rotatedCoordinates[1]);
    }

    /**
     * Get the x position of the point.
     * @return the x of the point.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y position of the point.
     * @return the y of the point.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the x position of this point
     * @param x the x of the point
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set the y position of this point
     * @param y the y of the point
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Get a string representation of a point.
     * @return a string representing the point.
     */
    @Override
    public String toString() {
        String pointRepresentation = "";
        return pointRepresentation.concat("(" + x + ", " + y + ")");
    }
}
