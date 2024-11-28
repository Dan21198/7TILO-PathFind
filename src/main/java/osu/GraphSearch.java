package osu;

import java.util.*;

class GraphSearch {
    private Map<String, Airport> airports;

    public GraphSearch(List<Airport> airportList) {
        airports = new HashMap<>();
        for (Airport airport : airportList) {
            airports.put(airport.code, airport);
        }
    }

    // Dijkstra's Algorithm
    public void findShortestPathDijkstra(String startCode, String endCode) {
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

            if (currentAirportCode.equals(endCode)) {
                break;
            }

            Airport currentAirport = airports.get(currentAirportCode);
            if (currentAirport == null) {
                continue;
            }

            for (Map.Entry<String, Double> connection : currentAirport.connections.entrySet()) {
                String neighborCode = connection.getKey();
                double distance = connection.getValue();

                double newDist = distances.get(currentAirportCode) + distance;

                if (newDist < distances.get(neighborCode)) {
                    distances.put(neighborCode, newDist);
                    previousAirports.put(neighborCode, currentAirportCode);
                    queue.add(new Node(neighborCode, newDist));
                }
            }
        }

        printShortestPath(previousAirports, startCode, endCode);
    }

    public void findShortestPathAStar(String startCode, String endCode) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        Map<String, Double> gScore = new HashMap<>();
        Map<String, Double> fScore = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();

        for (String code : airports.keySet()) {
            gScore.put(code, Double.MAX_VALUE);
            fScore.put(code, Double.MAX_VALUE);
        }

        gScore.put(startCode, 0.0);
        fScore.put(startCode, heuristic(startCode, endCode));

        openSet.add(new Node(startCode, gScore.get(startCode), fScore.get(startCode)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            String currentCode = currentNode.airportCode;

            if (currentCode.equals(endCode)) {
                printShortestPath(cameFrom, startCode, endCode);
                return;
            }

            Airport currentAirport = airports.get(currentCode);
            if (currentAirport == null) {
                continue;
            }

            for (Map.Entry<String, Double> connection : currentAirport.connections.entrySet()) {
                String neighborCode = connection.getKey();
                double tentativeGScore = gScore.get(currentCode) + connection.getValue();

                try {
                    double tentativeFScore = tentativeGScore + heuristic(neighborCode, endCode);

                    if (tentativeGScore < gScore.get(neighborCode)) {
                        cameFrom.put(neighborCode, currentCode);
                        gScore.put(neighborCode, tentativeGScore);
                        fScore.put(neighborCode, tentativeFScore);

                        if (openSet.stream().noneMatch(n -> n.airportCode.equals(neighborCode))) {
                            openSet.add(new Node(neighborCode, gScore.get(neighborCode), fScore.get(neighborCode)));
                        }
                    }
                } catch (IllegalStateException e) {
                    System.out.println("No direct connection found for " + currentCode + " to " + neighborCode);
                }
            }
        }

        System.out.println("No path found from " + startCode + " to " + endCode);
    }


    private double heuristic(String fromCode, String toCode) {
        Airport fromAirport = airports.get(fromCode);
        Airport toAirport = airports.get(toCode);

        if (fromAirport == null || toAirport == null) {
            throw new IllegalArgumentException("One or both of the airports are invalid: " + fromCode + ", " + toCode);
        }

        double fromConnections = fromAirport.connections.size();
        double toConnections = toAirport.connections.size();

        return Math.abs(fromConnections - toConnections);
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
        Airport fromAirport = airports.get(fromCode);

        if (fromAirport != null && fromAirport.connections.containsKey(toCode)) {
            return fromAirport.connections.get(toCode);
        }

        return Double.MAX_VALUE;
    }
}
