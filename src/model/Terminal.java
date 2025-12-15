package model;

public class Terminal {

    private String name;
    private double x;
    private double y;

    private String busStatus = "Not Arrived";
    private String lastArrivalTime = "--:--";
    private Terminal destination;

    public Terminal(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public String getName() { return name; }

   
    public String getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(String busStatus) {
        this.busStatus = busStatus;
    }

    public String getLastArrivalTime() {
        return lastArrivalTime;
    }

    public void setLastArrivalTime(String lastArrivalTime) {
        this.lastArrivalTime = lastArrivalTime;
    }

    public Terminal getDestination() {
        return destination;
    }

    public void setDestination(Terminal destination) {
        this.destination = destination;
    }
}
