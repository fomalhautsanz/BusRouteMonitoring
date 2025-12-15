package model;

import java.util.ArrayList;
import java.util.List;

public class Waypoint {

    private double x, y;
    private final List<LinkedList<Waypoint>.Node> connections = new ArrayList<>();

    public Waypoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public List<LinkedList<Waypoint>.Node> getConnections() {
        return connections;
    }

    public void connectTo(LinkedList<Waypoint>.Node node) {
        connections.add(node);
    }
}
