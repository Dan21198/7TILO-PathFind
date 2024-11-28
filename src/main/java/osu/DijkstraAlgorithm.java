package osu;

import java.util.*;


class DijkstraAlgorithm {
    private Map<String, Airport> airports;

    public DijkstraAlgorithm(List<Airport> airportList) {
        airports = new HashMap<>();
        for (Airport airport : airportList) {
            airports.put(airport.code, airport);
        }
    }

    public void findShortestPath(String startCode, String endCode) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousAirports = new HashMap<>();

        for (String code : airports.keySet()) {
            distances.put(code, Double.MAX_VALUE);
        }
        distances.put(startCode, 0.0);

        queue.add(new Node(startCode, 0.0));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            String currentAirportCode = currentNode.airportCode;

            // Debugging: Check the current airport code
            //System.out.println("Processing airport: " + currentAirportCode);

            Airport currentAirport = airports.get(currentAirportCode);
            if (currentAirport == null) {
                System.out.println("Error: Airport " + currentAirportCode + " not found.");
                continue;
            }

            if (currentAirportCode.equals(endCode)) {
                break;
            }

            for (Connection conn : currentAirport.connections) {
                double distance = conn.getDistance();

                double newDist = distances.get(currentAirportCode) + distance;
                String destinationCode = conn.getDestination();

                double existingDistance = distances.getOrDefault(destinationCode, Double.MAX_VALUE);

                if (newDist < existingDistance) {
                    distances.put(destinationCode, newDist);
                    previousAirports.put(destinationCode, currentAirportCode);
                    queue.add(new Node(destinationCode, newDist));
                }
            }
        }

        printShortestPath(previousAirports, startCode, endCode);
    }

    private void printShortestPath(Map<String, String> previousAirports, String startCode, String endCode) {
        List<String> path = new ArrayList<>();
        String current = endCode;

        while (current != null) {
            path.add(current);
            current = previousAirports.get(current);
        }

        Collections.reverse(path);

        System.out.print("Shortest path from " + startCode + " to " + endCode + ": ");
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            double dist = getDistance(from, to);
            totalDistance += dist;
            System.out.print(from + " -> " + to + " (" + String.format("%.4f", dist) + " km) -> ");
        }
        System.out.println("Total distance: " + String.format("%.4f", totalDistance) + " km");
    }

    private double getDistance(String fromCode, String toCode) {
        for (Connection conn : airports.get(fromCode).connections) {
            if (conn.destination.equals(toCode)) {
                return conn.distance;
            }
        }
        return 0.0;
    }
}
