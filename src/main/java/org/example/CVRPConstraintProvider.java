package org.example;

import ai.timefold.solver.core.api.score.stream.*;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

public class CVRPConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                vehicleCapacity(factory),
                distanceCost(factory)
        };
    }

    // HARD constraint: capacity
    private Constraint vehicleCapacity(ConstraintFactory factory) {
        return factory.forEach(Customer.class)
                .groupBy(
                        Customer::getVehicle,
                        ConstraintCollectors.sum(Customer::getDemand)
                )
                .filter((vehicle, totalDemand) ->
                        vehicle != null && totalDemand > vehicle.getCapacity()
                )
                .penalize(
                        HardSoftScore.ONE_HARD,
                        (vehicle, totalDemand) -> totalDemand - vehicle.getCapacity()
                )
                .asConstraint("vehicle capacity exceeded");
    }

    private Constraint vehicleChainConsistency(ConstraintFactory factory) {
        return factory.forEach(Customer.class)
                .filter(customer -> {
                    Standstill prev = customer.getPreviousStandstill();

                    if (prev == null) {
                        return true; // ❌ invalid
                    }

                    if (prev instanceof Customer c) {
                        return c.getVehicle() != customer.getVehicle();
                    }

                    return false;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("route consistency");
    }

    // SOFT constraint: distance
    private Constraint distanceCost(ConstraintFactory factory) {
        return factory.forEach(Customer.class)
                .penalize(
                        HardSoftScore.ONE_SOFT,
                        customer -> {
                            Standstill prev = customer.getPreviousStandstill();
                            if (prev == null) return 0;

                            return (int) prev.getLocation()
                                    .distanceTo(customer.getLocation());
                        }
                )
                .asConstraint("distance cost");
    }
}