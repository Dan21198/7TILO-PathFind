package osu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        // Path to your input JSON file
        String filePath = "src/main/java/osu/input.json";

        try {
            // Read JSON file contents into a string
            String jsonData = Files.readString(Path.of(filePath));

            // Parse the JSON
            JSONArray airportArray = new JSONArray(jsonData);

            // Create a list to store airports
            List<Airport> airports = new ArrayList<>();

            // Load airports from JSON
            for (int i = 0; i < airportArray.length(); i++) {
                JSONObject airportJson = airportArray.getJSONObject(i);
                String airportCode = airportJson.getString("code");
                String airportName = airportJson.getString("name");

                Airport airport = new Airport(airportCode, airportName);

                // Load connections for this airport
                JSONArray connectionsJson = airportJson.getJSONArray("connections");
                for (int j = 0; j < connectionsJson.length(); j++) {
                    JSONObject connectionJson = connectionsJson.getJSONObject(j);
                    String destinationCode = connectionJson.getString("code");

                    JSONObject distanceJson = connectionJson.getJSONObject("distance");
                    double distanceValue = distanceJson.getDouble("value");
                    String distanceUnit = distanceJson.getString("unit");

                    // Convert miles to kilometers
                    if (distanceUnit.equalsIgnoreCase("mi")) {
                        distanceValue *= 1.60934;
                        distanceUnit = "km";
                    }

                    airport.addConnection(destinationCode, distanceValue);
                }

                airports.add(airport);
            }

            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(airports);
            dijkstra.findShortestPath("PRG", "DEL");
            dijkstra.findShortestPath("BRE", "FNJ");
            dijkstra.findShortestPath("JFK", "CAI");
            dijkstra.findShortestPath("DUB", "DME");
            dijkstra.findShortestPath("OKA", "EVX");

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
