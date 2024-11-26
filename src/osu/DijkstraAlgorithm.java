package osu;

import java.util.*;


class DijkstraAlgorithm {
    private Map<String, Airport> airports;

    public DijkstraAlgorithm(List<Airport> airportList) {
        // Initialize the airport map
        airports = new HashMap<>();
        for (Airport airport : airportList) {
            airports.put(airport.code, airport);  // Store airports by their code
        }
    }

    public void findShortestPath(String startCode, String endCode) {
        // Priority queue to store nodes (airports with their distance)
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousAirports = new HashMap<>();

        // Initialize distances to infinity and the start airport to 0
        for (String code : airports.keySet()) {
            distances.put(code, Double.MAX_VALUE);  // Set all distances to infinity
        }
        distances.put(startCode, 0.0);  // Distance to start airport is 0

        // Start with the source airport
        queue.add(new Node(startCode, 0.0));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            String currentAirportCode = currentNode.airportCode;

            // Debugging: Check the current airport code
            System.out.println("Processing airport: " + currentAirportCode);

            // Check if the current airport exists in the map
            Airport currentAirport = airports.get(currentAirportCode);
            if (currentAirport == null) {
                System.out.println("Error: Airport " + currentAirportCode + " not found.");
                continue; // Skip this iteration if the airport doesn't exist
            }

            // If we reached the destination, stop the algorithm
            if (currentAirportCode.equals(endCode)) {
                break;
            }

            // Relax edges
            for (Connection conn : currentAirport.connections) {
                // Access the distance using the getDistance() method
                double distance = conn.getDistance();
                System.out.println("Distance to " + conn.getDestination() + ": " + distance + " km");

                // Now you can use the distance for your Dijkstra algorithm
                double newDist = distances.get(currentAirportCode) + distance;
                String destinationCode = conn.getDestination();

                // Safely retrieve the existing distance or assume infinity if not found
                double existingDistance = distances.getOrDefault(destinationCode, Double.MAX_VALUE);

                // Perform the relaxation step
                if (newDist < existingDistance) {
                    distances.put(destinationCode, newDist);
                    previousAirports.put(destinationCode, currentAirportCode);
                    queue.add(new Node(destinationCode, newDist));
                }
            }
        }

        // Output the shortest path
        printShortestPath(previousAirports, startCode, endCode, distances);
    }

    private void printShortestPath(Map<String, String> previousAirports, String startCode, String endCode, Map<String, Double> distances) {
        List<String> path = new ArrayList<>();
        String current = endCode;

        // Trace the path from the destination to the source
        while (current != null) {
            path.add(current);
            current = previousAirports.get(current);
        }

        // Reverse the path to get it from start to end
        Collections.reverse(path);

        // Print the path and the total distance
        System.out.print("Shortest path from " + startCode + " to " + endCode + ": ");
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            double dist = getDistance(from, to);
            totalDistance += dist;
            System.out.print(from + " -> " + to + " (" + dist + " km) -> ");
        }
        System.out.println("Total distance: " + totalDistance + " km");
    }

    private double getDistance(String fromCode, String toCode) {
        // Get the connection distance from the 'fromCode' airport to the 'toCode' airport
        for (Connection conn : airports.get(fromCode).connections) {
            if (conn.destination.equals(toCode)) {
                return conn.distance;
            }
        }
        return 0.0;  // Return 0 if no connection found
    }
}
