package org.example;

import ai.timefold.solver.core.api.domain.solution.*;
import ai.timefold.solver.core.api.domain.valuerange.*;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class CVRPSolution {

    @ValueRangeProvider(id = "vehicleRange")
    private List<Vehicle> vehicleList;

    @ValueRangeProvider(id = "standstillRange")
    private List<Standstill> standstillList;

    @PlanningEntityCollectionProperty
    private List<Customer> customerList;

    private Depot depot;

    @PlanningScore
    private HardSoftScore score;

    public CVRPSolution() {}

    public CVRPSolution(List<Vehicle> vehicleList,
                        List<Standstill> standstillList,
                        List<Customer> customerList,
                        Depot depot) {
        this.vehicleList = vehicleList;
        this.standstillList = standstillList;
        this.customerList = customerList;
        this.depot = depot;
    }

    public List<Vehicle> getVehicleList() { return vehicleList; }

    public List<Standstill> getStandstillList() { return standstillList; }

    public List<Customer> getCustomerList() { return customerList; }

    public Depot getDepot() { return depot; }

    public HardSoftScore getScore() { return score; }
}
