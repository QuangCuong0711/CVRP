package org.example;
import java.util.HashMap;
import java.util.*;
import java.nio.file.*;
import java.io.*;


public class CvrplibReader {

    public static CVRPSolution read(Path file) throws IOException {

        List<Customer> customers = new ArrayList<>();
        List<Standstill> standstills = new ArrayList<>();

        Depot depot = null;
        List<Vehicle> vehicles = new ArrayList<>();

        List<String> lines = Files.readAllLines(file);

        boolean nodeSection = false;
        boolean demandSection = false;

        Map<Integer, Location> coords = new HashMap<>();
        Map<Integer, Integer> demands = new HashMap<>();

        for (String line : lines) {

            if (line.startsWith("NODE_COORD_SECTION")) {
                nodeSection = true;
                continue;
            }
            if (line.startsWith("DEMAND_SECTION")) {
                nodeSection = false;
                demandSection = true;
                continue;
            }

            if (line.startsWith("DEPOT_SECTION")) break;

            if (nodeSection) {
                String[] p = line.trim().split("\\s+");
                if (p.length >= 3) {
                    coords.put(Integer.parseInt(p[0]),
                            new Location(Double.parseDouble(p[1]),
                                    Double.parseDouble(p[2])));
                }
            }

            if (demandSection) {
                String[] p = line.trim().split("\\s+");
                if (p.length >= 2) {
                    demands.put(Integer.parseInt(p[0]),
                            Integer.parseInt(p[1]));
                }
            }
        }

        // depot = node 1
        depot = new Depot(coords.get(1));

        // build vehicles (simple heuristic)
        vehicles.add(new Vehicle(1, 100, depot));
        vehicles.add(new Vehicle(2, 100, depot));

        standstills.addAll(vehicles);
        standstills.add(depot);

        for (int id : coords.keySet()) {
            if (id == 1) continue;

            Customer c = new Customer(
                    id,
                    coords.get(id),
                    demands.getOrDefault(id, 0)
            );

            customers.add(c);
        }

        return new CVRPSolution(
                vehicles,
                standstills,
                customers,
                depot
        );
    }
}