package org.example;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;

import java.nio.file.Path;
import java.time.Duration;


public class Main {
    public static void main(String[] args) throws Exception {

        CVRPSolution problem =
                CvrplibReader.read(Path.of("src/main/resources/A-n32-k5.vrp"));

        SolverFactory<CVRPSolution> factory =
                SolverFactory.create(new SolverConfig()
                        .withSolutionClass(CVRPSolution.class)
                        .withEntityClasses(Customer.class)
                        .withConstraintProviderClass(CVRPConstraintProvider.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(30))
                );

        Solver<CVRPSolution> solver = factory.buildSolver();

        CVRPSolution solved = solver.solve(problem);

        System.out.println("Score: " + solved.getScore());
    }
}