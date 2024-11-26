package osu;

import java.util.ArrayList;
import java.util.List;

public class Airport {
    String code;
    String name;
    List<Connection> connections;

    public Airport(String code, String name) {
        this.code = code;
        this.name = name;
        this.connections = new ArrayList<>();
    }

    public void addConnection(String destination, double distance) {
        this.connections.add(new Connection(destination, distance));
    }
}

