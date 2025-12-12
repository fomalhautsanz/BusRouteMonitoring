package model;

public class Terminal {
    private String name;
    private double x;      
    private double y;      
    private LinkedList<Route> routes;

    public Terminal(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.routes = new LinkedList<>();
    }

    public String getName() { return name; }

    public double getX(double mapWidth) { return x; }
    public double getY(double mapHeight) { return y; }
}
