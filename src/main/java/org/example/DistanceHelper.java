package org.example;

public class DistanceHelper {
    public static long distance(Standstill a, Standstill b) {
        double dx = a.getLocation().x() - b.getLocation().x();
        double dy = a.getLocation().y() - b.getLocation().y();
        return Math.round(Math.sqrt(dx * dx + dy * dy));
    }
}
