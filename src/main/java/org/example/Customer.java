package org.example;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.*;

@PlanningEntity
public class Customer implements Standstill {

    private long id;
    private Location location;
    private int demand;

    /**
     * Chained planning variable.
     * Value range = vehicleRange (anchors) + customerRange (other nodes).
     * Hai range này được Timefold merge tự động khi liệt kê trong valueRangeProviderRefs.
     */
    @PlanningVariable(
            valueRangeProviderRefs = {"vehicleRange", "customerRange"},
            graphType = PlanningVariableGraphType.CHAINED
    )
    private Standstill previousStandstill;

    /**
     * Shadow variable: Timefold tự tính bằng cách đi ngược chain đến anchor (Vehicle).
     */
    @AnchorShadowVariable(sourceVariableName = "previousStandstill")
    private Vehicle vehicle;

    public Customer() {}

    public Customer(long id, Location location, int demand) {
        this.id = id;
        this.location = location;
        this.demand = demand;
    }

    public long getId() { return id; }

    @Override
    public Location getLocation() { return location; }

    public int getDemand() { return demand; }

    public Standstill getPreviousStandstill() { return previousStandstill; }

    public void setPreviousStandstill(Standstill previousStandstill) {
        this.previousStandstill = previousStandstill;
    }

    public Vehicle getVehicle() { return vehicle; }

    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public String toString() { return "Customer#" + id; }
}