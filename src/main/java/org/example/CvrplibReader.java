package org.example;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class CvrplibReader {

    public static CVRPSolution read(Path file) throws IOException {

        List<String> lines = Files.readAllLines(file);

        int numTrucks = 5;
        int capacity  = 100;

        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("CAPACITY")) {
                capacity  = Integer.parseInt(t.replaceAll("[^0-9]", "").trim());
            }
            if (t.startsWith("TRUCKS")) {
                numTrucks = Integer.parseInt(t.replaceAll("[^0-9]", "").trim());
            }
        }

        Map<Integer, Location> coords  = new LinkedHashMap<>();
        Map<Integer, Integer>  demands = new LinkedHashMap<>();
        boolean nodeSection   = false;
        boolean demandSection = false;

        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("NODE_COORD_SECTION")) { nodeSection = true;  demandSection = false; continue; }
            if (t.startsWith("DEMAND_SECTION"))     { demandSection = true; nodeSection  = false; continue; }
            if (t.startsWith("DEPOT_SECTION"))      break;
            if (t.isEmpty() || t.equals("EOF"))     continue;

            String[] p = t.split("\\s+");
            if (nodeSection && p.length >= 3)
                coords.put(Integer.parseInt(p[0]),
                        new Location(Double.parseDouble(p[1]), Double.parseDouble(p[2])));
            if (demandSection && p.length >= 2)
                demands.put(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
        }

        Depot depot = new Depot(coords.get(1));

        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 1; i <= numTrucks; i++)
            vehicles.add(new Vehicle(i, capacity, depot));

        // customerList only — NO mixed standstillList anymore
        List<Customer> customers = new ArrayList<>();
        for (int id : coords.keySet()) {
            if (id == 1) continue;
            customers.add(new Customer(id, coords.get(id), demands.getOrDefault(id, 0)));
        }

        return new CVRPSolution(vehicles, customers, depot);
    }
}