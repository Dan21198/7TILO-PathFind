package osu;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        // JSON data
        String jsonData = "[{\"name\":\"Václav Havel Airport Prague\",\"code\":\"PRG\",\"connections\":[{\"code\":\"BRE\",\"distance\":{\"value\":500.63,\"unit\":\"km\"}},{\"code\":\"LHR\",\"distance\":{\"value\":1044.93,\"unit\":\"km\"}},{\"code\":\"RSW\",\"distance\":{\"value\":8184.96,\"unit\":\"km\"}}]},{\"name\":\"Heathrow Airport\",\"code\":\"LHR\",\"connections\":[{\"code\":\"PRG\",\"distance\":{\"value\":1044.93,\"unit\":\"km\"}},{\"code\":\"PEK\",\"distance\":{\"value\":8156.63,\"unit\":\"km\"}}]}]"; // shortened for brevity

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
                double distance = connectionJson.getJSONObject("distance").getDouble("value");

                airport.addConnection(destinationCode, distance);
            }

            airports.add(airport);
        }

        // Run Dijkstra's algorithm
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(airports);
        dijkstra.findShortestPath("PRG", "LHR");  // Example: Find path from PRG to LHR
    }
}
