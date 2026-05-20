package org.example;

import ai.timefold.solver.core.api.solver.*;
import ai.timefold.solver.core.config.solver.SolverConfig;

import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        CVRPSolution problem =
                CvrplibReader.read(Path.of("src/main/resources/A-n32-k5.vrp"));

        System.out.println("Vehicles : " + problem.getVehicleList().size());
        System.out.println("Customers: " + problem.getCustomerList().size());

        SolverFactory<CVRPSolution> factory =
                SolverFactory.create(new SolverConfig()
                        .withSolutionClass(CVRPSolution.class)
                        .withEntityClasses(Customer.class)
                        .withConstraintProviderClass(CVRPConstraintProvider.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(30))
                );

        Solver<CVRPSolution> solver = factory.buildSolver();
        CVRPSolution solved = solver.solve(problem);

        System.out.println("\n=== RESULT ===");
        System.out.println("Score: " + solved.getScore());

        // Build route map grouped by vehicle
        Map<Vehicle, List<Customer>> routes = new LinkedHashMap<>();
        for (Vehicle v : solved.getVehicleList()) routes.put(v, new ArrayList<>());
        for (Customer c : solved.getCustomerList())
            if (c.getVehicle() != null) routes.get(c.getVehicle()).add(c);

        for (Map.Entry<Vehicle, List<Customer>> entry : routes.entrySet()) {
            Vehicle v = entry.getKey();
            List<Customer> route = entry.getValue();

            // Sort by chain order (walk from vehicle anchor)
            List<Customer> ordered = new ArrayList<>();
            Standstill cur = v;
            for (int i = 0; i < route.size(); i++) {
                for (Customer c : route) {
                    if (c.getPreviousStandstill() == cur) {
                        ordered.add(c);
                        cur = c;
                        break;
                    }
                }
            }

            int load = ordered.stream().mapToInt(Customer::getDemand).sum();
            System.out.printf("Vehicle %d (cap %3d, load %3d): Depot → %s → Depot%n",
                    v.getId(), v.getCapacity(), load,
                    ordered.stream()
                            .map(c -> "C" + c.getId())
                            .reduce((a, b) -> a + " → " + b)
                            .orElse("(empty)"));
        }
    }
}