package osu;

import java.util.HashMap;
import java.util.Map;

public class Airport {
    String code;
    String name;
    Map<String, Double> connections;

    public Airport(String name, String code) {
        this.name = name;
        this.code = code;
        this.connections = new HashMap<>();
    }
}

