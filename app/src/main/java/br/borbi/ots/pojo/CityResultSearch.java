package br.borbi.ots.pojo;

/**
 * Created by Gabriela on 14/07/2015.
 */
public class CityResultSearch implements Comparable<CityResultSearch>{

    private City city;
    private Integer distance;
    private Integer idResultSearch;

    public CityResultSearch(City city, Integer distance, Integer idResultSearch) {
        this.city = city;
        this.distance = distance;
        this.idResultSearch = idResultSearch;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getIdResultSearch() {
        return idResultSearch;
    }

    public void setIdResultSearch(Integer idResultSearch) {
        this.idResultSearch = idResultSearch;
    }

    @Override
    public String toString() {
        return "CityResultSearch{" +
                "city=" + city +
                ", distance=" + distance +
                ", idResultSearch=" + idResultSearch +
                '}';
    }

    @Override
    public int compareTo(CityResultSearch another) {
        return this.getDistance().compareTo(another.getDistance());
    }
}
