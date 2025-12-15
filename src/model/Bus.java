package model;

import java.util.List;

public class Bus {

    private double x, y;
    private double speed;
    private boolean isMoving;

    private LinkedList<Waypoint>.Node currentNode;
    private LinkedList<Waypoint>.Node endNode;
    private double endX, endY;

    public Bus(LinkedList<Waypoint>.Node startNode, LinkedList<Waypoint>.Node endNode, double speed) {
        this.currentNode = startNode;
        this.endNode = endNode;
        this.speed = speed;
        this.isMoving = true;

        if (currentNode != null) {
            this.x = currentNode.data.getX();
            this.y = currentNode.data.getY();
        }

        if (endNode != null) {
            this.endX = endNode.data.getX();
            this.endY = endNode.data.getY();
        }
    }

    public Bus(List<Waypoint> waypoints, double speed) {
        if (waypoints == null || waypoints.size() < 2) {
            isMoving = false;
            return;
        }

        this.speed = speed;
        this.isMoving = true;

        LinkedList<Waypoint> tempList = new LinkedList<>();

        for (Waypoint wp : waypoints) {
            tempList.add(wp);
        }

        this.currentNode = tempList.getHead();
        this.endNode = tempList.getTail();

        this.x = currentNode.data.getX();
        this.y = currentNode.data.getY();

        this.endX = endNode.data.getX();
        this.endY = endNode.data.getY();
    }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isMoving() { return isMoving; }
    public LinkedList<Waypoint>.Node getCurrentNode() { return currentNode; }
    public LinkedList<Waypoint>.Node getEndNode() { return endNode; }

    public void updatePosition() {
        if (!isMoving || currentNode == null) return;

        Waypoint target = currentNode.data;

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx*dx + dy*dy);

        if (distance <= speed) {
            x = target.getX();
            y = target.getY();

            // stop at destination
            if (Math.abs(x - endX) < 0.1 && Math.abs(y - endY) < 0.1) {
                isMoving = false;
            } else {
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
            }
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }

    public void moveBackward() {
        if (currentNode == null || currentNode.prev == null) return;

        // Move to pevious waypoint
        currentNode = currentNode.prev;
        x = currentNode.data.getX();
        y = currentNode.data.getY();
        isMoving = true;
    }

    public void skipForward(int steps) {
        for (int i = 0; i < steps && currentNode != null; i++) {
            // Move to the next waypoint directly
            if (currentNode.next != null) {
                currentNode = currentNode.next;
                x = currentNode.data.getX();
                y = currentNode.data.getY();

                // Check if we reached the end
                if (Math.abs(x - endX) < 0.1 && Math.abs(y - endY) < 0.1) {
                    isMoving = false;
                    break;
                } else {
                    isMoving = true;
                }
            }
        }
    }

    public void skipBackward(int steps) {
        for (int i = 0; i < steps && currentNode != null; i++) {
            if (currentNode.prev != null) {
                currentNode = currentNode.prev;
                x = currentNode.data.getX();
                y = currentNode.data.getY();
                isMoving = true;
            }
        }
    }

    public void reset(LinkedList<Waypoint>.Node startNode) {
        this.currentNode = startNode;
        if (currentNode != null) {
            this.x = currentNode.data.getX();
            this.y = currentNode.data.getY();
        }
        this.isMoving = true;
    }
}