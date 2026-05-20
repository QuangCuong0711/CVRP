package org.example;

public class Vehicle implements Standstill {
    private long id;
    private int capacity;
    private Depot depot;

    public Vehicle(long id, int capacity, Depot depot) {
        this.id = id;
        this.capacity = capacity;
        this.depot = depot;
    }

    @Override
    public Location getLocation() {
        return depot.getLocation();
    }

    public long getId() { return id; }
    public int getCapacity() { return capacity; }
    public Depot getDepot() { return depot; }
}
