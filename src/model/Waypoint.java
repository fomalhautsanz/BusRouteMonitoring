package model;

public class Waypoint {

    private double x; // X position (pixels or %)
    private double y; // Y position (pixels or %)
    private String name; // Optional: waypoint name

    public Waypoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Waypoint(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public String getName() { return name; }

    // Optional setters
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setName(String name) { this.name = name; }
}