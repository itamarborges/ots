package br.borbi.ots.pojo;

import br.borbi.ots.utility.CoordinatesUtillity;

/**
 * Created by Gabriela on 05/06/2015.
 */
public class Coordinates {

    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    public Coordinates(double lastLatitude, double lastLongitude, double distance) {
        this.minLatitude = CoordinatesUtillity.getMinLatitude(lastLatitude,distance);
        this.maxLatitude = CoordinatesUtillity.getMaxLatitude(lastLatitude, distance);

        this.minLongitude = CoordinatesUtillity.getMinLongitude(lastLatitude, lastLongitude, distance);
        this.maxLongitude = CoordinatesUtillity.getMaxLongitude(lastLatitude, lastLongitude, distance);

        if(this.minLatitude > this.maxLatitude){
            double aux = this.maxLatitude;
            this.maxLatitude = this.minLatitude;
            this.minLatitude = aux;
        }

        if(this.minLongitude > this.maxLongitude){
            double aux = this.maxLongitude;
            this.maxLongitude = this.minLongitude;
            this.minLongitude = aux;
        }
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }
}
