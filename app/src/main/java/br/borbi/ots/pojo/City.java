package br.borbi.ots.pojo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gabriela on 28/05/2015.
 */
public class City implements Serializable{

    private static final long serialVersionUID = 42L;

    private long id;
    private String name;
    private String countryCode;
    private String countryName;
    private List<DayForecast> dayForecasts;
    private Double latitude;
    private Double longitude;

    public City(long id, String name, String countryCode, List<DayForecast> dayForecasts) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.dayForecasts = dayForecasts;
    }

    public City(long id, String name, String countryCode) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
    }

    public City(long id, String name, String countryCode, String countryName, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DayForecast> getDayForecasts() {
        return dayForecasts;
    }

    public void setDayForecasts(List<DayForecast> dayForecasts) {
        this.dayForecasts = dayForecasts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {

        String retorno = "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", dayForecasts=";

        if (dayForecasts == null) {
            retorno += " vazio";
        } else {
            Iterator<DayForecast> it = dayForecasts.iterator();
            while (it.hasNext()) {
                DayForecast dayForecast = (DayForecast) it.next();
                retorno += dayForecast.toString();
            }
        }

        retorno += "}";

        return retorno;
    }
}
