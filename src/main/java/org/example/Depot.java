package org.example;

public class Depot implements Standstill {
    private Location location;

    public Depot(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
