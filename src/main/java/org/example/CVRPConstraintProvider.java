package org.example;

import ai.timefold.solver.core.api.score.stream.*;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

public class CVRPConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                vehicleCapacity(factory),
                distanceCost(factory),
                returnToDepotCost(factory)
        };
    }

    // HARD: tổng demand trên mỗi xe không vượt capacity
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

    // SOFT: khoảng cách từ previous → customer (toàn bộ các cạnh nội tuyến)
    private Constraint distanceCost(ConstraintFactory factory) {
        return factory.forEach(Customer.class)
                .penalize(
                        HardSoftScore.ONE_SOFT,
                        customer -> {
                            Standstill prev = customer.getPreviousStandstill();
                            if (prev == null) return 0;
                            return (int) Math.round(
                                    prev.getLocation().distanceTo(customer.getLocation()));
                        }
                )
                .asConstraint("distance cost");
    }

    // SOFT: khoảng cách từ customer cuối của mỗi xe quay về depot
    // Customer "cuối" = không có Customer nào trỏ vào nó làm previousStandstill
    private Constraint returnToDepotCost(ConstraintFactory factory) {
        return factory.forEach(Customer.class)
                // Chỉ giữ lại customer KHÔNG có customer nào đứng sau nó
                .ifNotExists(Customer.class,
                        Joiners.equal(c -> c, Customer::getPreviousStandstill))
                .filter(customer -> customer.getVehicle() != null)
                .penalize(
                        HardSoftScore.ONE_SOFT,
                        customer -> (int) Math.round(
                                customer.getLocation()
                                        .distanceTo(customer.getVehicle().getDepot().getLocation()))
                )
                .asConstraint("return to depot cost");
    }
}