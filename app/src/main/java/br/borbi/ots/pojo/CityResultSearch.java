package br.borbi.ots.pojo;

import java.io.Serializable;
import java.util.List;

import br.borbi.ots.enums.WeatherForecastSourcePriority;

/**
 * Created by Gabriela on 14/07/2015.
 */
public class CityResultSearch implements Comparable<CityResultSearch>, Serializable {

    private City city;
    private Integer distance;
    private Integer idResultSearch;
    private boolean isFirstCity;
    private List<DayForecast> dayForecasts;
    private WeatherForecastSourcePriority weatherForecastSourceUsed;

    public CityResultSearch(City city, Integer distance, Integer idResultSearch, WeatherForecastSourcePriority weatherSource) {
        this.city = city;
        this.distance = distance;
        this.idResultSearch = idResultSearch;
        this.weatherForecastSourceUsed = weatherSource;
    }

    public CityResultSearch(City city, List<DayForecast> dayForecasts) {
        this.city = city;
        this.dayForecasts = dayForecasts;
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

    public boolean isFirstCity() {
        return isFirstCity;
    }

    public void setIsFirstCity(boolean isFirstCity) {
        this.isFirstCity = isFirstCity;
    }

    public List<DayForecast> getDayForecasts() {
        return dayForecasts;
    }

    public void setDayForecasts(List<DayForecast> dayForecasts) { this.dayForecasts = dayForecasts; }

    public WeatherForecastSourcePriority getWeatherForecastSourceUsed() {
        return weatherForecastSourceUsed;
    }

    public void setWeatherForecastSourceUsed(WeatherForecastSourcePriority weatherForecastSourceUsed) {
        this.weatherForecastSourceUsed = weatherForecastSourceUsed;
    }
}
