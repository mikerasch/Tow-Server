package edu.uwp.appfactory.tow.controllers.location;

import org.springframework.stereotype.Component;

@Component
public class CalculateDistanceService {
    public final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculates the distance between two points on the Earth's surface using the Haversine formula.
     *
     * @param lat1 the latitude of the first point in degrees
     * @param lon1 the longitude of the first point in degrees
     * @param lat2 the latitude of the second point in degrees
     * @param lon2 the longitude of the second point in degrees
     * @return the distance between the two points in kilometers
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
