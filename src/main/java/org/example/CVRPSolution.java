package org.example;

import ai.timefold.solver.core.api.domain.solution.*;
import ai.timefold.solver.core.api.domain.valuerange.*;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class CVRPSolution {

    /**
     * vehicleList = anchors của chain.
     * Đây là một phần của value range cho previousStandstill.
     */
    @ValueRangeProvider(id = "vehicleRange")
    private List<Vehicle> vehicleList;

    /**
     * customerList = planning entities VÀ đồng thời là nodes trong chain.
     * @PlanningEntityCollectionProperty đăng ký chúng là entity.
     * @ValueRangeProvider đăng ký chúng là giá trị hợp lệ cho previousStandstill
     * (customer có thể đứng sau customer khác trong cùng route).
     *
     * Timefold hỗ trợ một list vừa là entity vừa là value range —
     * đây là cách chuẩn cho chained CVRP.
     */
    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "customerRange")
    private List<Customer> customerList;

    private Depot depot;

    @PlanningScore
    private HardSoftScore score;

    public CVRPSolution() {}

    public CVRPSolution(List<Vehicle> vehicleList,
                        List<Customer> customerList,
                        Depot depot) {
        this.vehicleList = vehicleList;
        this.customerList = customerList;
        this.depot = depot;
    }

    public List<Vehicle> getVehicleList() { return vehicleList; }

    public List<Customer> getCustomerList() { return customerList; }

    public Depot getDepot() { return depot; }

    public HardSoftScore getScore() { return score; }
}