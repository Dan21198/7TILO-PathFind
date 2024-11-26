package osu;

public class Connection {
    String destination;
    double distance;

    public Connection(String destination, double distance) {
        this.destination = destination;
        this.distance = distance;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }
}

