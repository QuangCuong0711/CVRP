package org.example;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.*;

@PlanningEntity
public class Customer implements Standstill {

    private long id;
    private Location location;
    private int demand;

    @PlanningVariable(valueRangeProviderRefs = "standstillRange")
    private Standstill previousStandstill;

    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    private Vehicle vehicle;

    public Customer() {}

    public Customer(long id, Location location, int demand) {
        this.id = id;
        this.location = location;
        this.demand = demand;
    }

    public long getId() { return id; }

    public Location getLocation() { return location; }

    public int getDemand() { return demand; }

    public Standstill getPreviousStandstill() {
        return previousStandstill;
    }

    public void setPreviousStandstill(Standstill previousStandstill) {
        this.previousStandstill = previousStandstill;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}