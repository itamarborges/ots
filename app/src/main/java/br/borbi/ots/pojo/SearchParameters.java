package br.borbi.ots.pojo;

import java.util.List;

/**
 * Created by Gabriela on 08/06/2015.
 */
public class SearchParameters {

    private int numberOfDays;
    private List<City> cities;

    public SearchParameters(int numberOfDays, List<City> cities) {
        this.numberOfDays = numberOfDays;
        this.cities = cities;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
