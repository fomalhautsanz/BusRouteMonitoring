package model;

public class Route {
    private Terminal start;
    private Terminal end;
    private LinkedList<Waypoint> waypoints; // or LinkedList<Waypoint>

    public Route(Terminal start, Terminal end) {
        this.start = start;
        this.end = end;
        this.waypoints = new LinkedList<>();
    }

    public Terminal getStart() { return start; }
    public Terminal getEnd() { return end; }
    public LinkedList<Waypoint> getWaypoints() { return waypoints; }
}