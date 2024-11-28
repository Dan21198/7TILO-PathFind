package osu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {

        String filePath = "src/main/java/osu/input.json";
        List<Airport> airports = new ArrayList<>();

        try {
            String jsonData = Files.readString(Path.of(filePath));

            JSONArray airportArray = new JSONArray(jsonData);

            for (int i = 0; i < airportArray.length(); i++) {
                JSONObject airportJson = airportArray.getJSONObject(i);
                String airportCode = airportJson.getString("code");
                String airportName = airportJson.getString("name");

                Airport airport = new Airport(airportName, airportCode);

                JSONArray connectionsJson = airportJson.getJSONArray("connections");
                for (int j = 0; j < connectionsJson.length(); j++) {
                    JSONObject connectionJson = connectionsJson.getJSONObject(j);
                    String destinationCode = connectionJson.getString("code");

                    JSONObject distanceJson = connectionJson.getJSONObject("distance");
                    double distanceValue = distanceJson.getDouble("value");
                    String distanceUnit = distanceJson.getString("unit");

                    if (distanceUnit.equalsIgnoreCase("mi")) {
                        distanceValue *= 1.60934;
                    }

                    airport.connections.put(destinationCode, distanceValue);
                }

                airports.add(airport);
            }

        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing JSON data: " + e.getMessage());
        }

        GraphSearch dijkstra = new GraphSearch(airports);
        System.out.println("Dijkstra's Algorithm: ");
        dijkstra.findShortestPathDijkstra("PRG", "DEL");
        dijkstra.findShortestPathDijkstra("BRE", "FNJ");
        dijkstra.findShortestPathDijkstra("JFK", "CAI");
        dijkstra.findShortestPathDijkstra("DUB", "DME");
        dijkstra.findShortestPathDijkstra("OKA", "EVX");
        System.out.println("A* Algorithm: ");
        dijkstra.findShortestPathAStar("PRG", "DEL");
        dijkstra.findShortestPathAStar("BRE", "FNJ");
        dijkstra.findShortestPathAStar("JFK", "CAI");
        dijkstra.findShortestPathAStar("DUB", "DME");
        dijkstra.findShortestPathAStar("OKA", "EVX");

    }
}
