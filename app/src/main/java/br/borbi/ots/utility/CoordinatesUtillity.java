package br.borbi.ots.utility;

import android.util.Log;

/**
 * Created by Gabriela on 23/05/2015.
 */
public class CoordinatesUtillity {

    private static final String LOG_TAG = CoordinatesUtillity.class.getSimpleName();

    private static final double EARTH_RADIUS = 6371d;

    public static double getMinLatitude(double originLatitude,double distance){
        return getLatitude(0, originLatitude,distance);
    }

    public static double getMaxLatitude(double originLatitude, double distance){
        return getLatitude(180, originLatitude,distance);
    }

    public static double getMinLongitude(double originLatitude, double originLongitude, double distance){
        return getLongitude(90, originLongitude, originLatitude, distance);
    }

    public static double getMaxLongitude(double originLatitude, double originLongitude, double distance){
        return getLongitude(270,originLongitude,originLatitude,distance);
    }

    private static double getLatitude(double bearing, double originLatitude,double distance) {

        double angularDistance = getAngularDistance(distance);

        originLatitude = Math.toRadians(originLatitude);
        bearing = Math.toRadians(bearing);

        double latitude = Math.asin((Math.sin(originLatitude) * Math.cos(angularDistance))
                + (Math.cos(originLatitude) * Math.sin(angularDistance) * Math.cos(bearing)));

        latitude = Math.toDegrees(latitude);

        return latitude;
    }

    private static double getLongitude(double bearing, double originLongitude,
                                       double originLatitude,  double distance) {

        double angularDistance = getAngularDistance(distance);

        originLongitude = Math.toRadians(originLongitude);
        originLatitude = Math.toRadians(originLatitude);
        double finalLatitude = Math.toRadians(getLatitude(bearing,originLatitude,distance));

        bearing = Math.toRadians(bearing);

        double longitude = originLongitude
                + Math.atan2(Math.sin(bearing) * Math.sin(angularDistance)
                        * Math.cos(originLatitude),
                Math.cos(angularDistance)
                        - (Math.sin(originLatitude) * Math.sin(finalLatitude)));

        longitude = Math.toDegrees(longitude);

        return longitude;
    }

    private static double getAngularDistance(double distance){
        return distance / EARTH_RADIUS;
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1)
                * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
}
