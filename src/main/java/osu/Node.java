package osu;

class Node {
    String airportCode;
    double distance;
    double fScore;

    public Node(String airportCode, double distance) {
        this.airportCode = airportCode;
        this.distance = distance;
    }

    public Node(String airportCode, double distance, double fScore) {
        this.airportCode = airportCode;
        this.distance = distance;
        this.fScore = fScore;
    }
}
