package model;

public class Bus {

    private String id;               
    private Route currentRoute;     
    private int currentWaypointIndex; 
    private double x;               
    private double y;               
    private double speed;            
    private boolean isMoving;      

    public Bus(Route route, double speed) {
        this.currentRoute = route;
        this.speed = speed;
        this.currentWaypointIndex = 0;
        this.isMoving = true;

        if (route.getWaypoints().getHead() != null) {
            this.x = route.getWaypoints().getHead().data.getX();
            this.y = route.getWaypoints().getHead().data.getY();
        }
    }

    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isMoving() { return isMoving; }
    public Route getCurrentRoute() { return currentRoute; }

    public void start() {
        isMoving = true;
    }

    public void stop() {
        isMoving = false;
    }

    public void updatePosition() {
        if (!isMoving || currentRoute == null) return;

        LinkedList<Waypoint>.Node currentNode = currentRoute.getWaypoints().getHead();
        int index = 0;
        while (index < currentWaypointIndex && currentNode != null) {
            currentNode = currentNode.next;
            index++;
        }

        if (currentNode == null) {
            stop(); 
            return;
        }

        Waypoint target = currentNode.data;

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx*dx + dy*dy);

        if (distance <= speed) {
            x = target.getX();
            y = target.getY();
            currentWaypointIndex++; // Move to next waypoint
        } else {
            x += dx / distance * speed;
            y += dy / distance * speed;
        }
    }
}

