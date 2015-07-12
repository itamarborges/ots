package br.borbi.ots.pojo;

import java.io.Serializable;
import java.util.Date;

import br.borbi.ots.enums.WeatherType;

/**
 * Created by Gabriela on 28/05/2015.
 */
public class DayForecast implements Serializable{

    private static final long serialVersionUID = -5659072624209865929L;

    private Date date;
    private Double minTemperature;
    private Double maxTemperature;
    private Double morningTemperature;
    private Double eveningTemperature;
    private Double nightTemperature;
    private WeatherType weatherType;
    private Double precipitation;
    private Double humidity;
    private Integer id;

    public DayForecast(Date date, Double minTemperature, Double maxTemperature, Double morningTemperature, Double eveningTemperature, Double nightTemperature, WeatherType weatherType, Double precipitation, Double humidity) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.morningTemperature = morningTemperature;
        this.eveningTemperature = eveningTemperature;
        this.nightTemperature = nightTemperature;
        this.weatherType = weatherType;
        this.precipitation = precipitation;
        this.humidity = humidity;
    }

    public DayForecast(Date date, Double minTemperature, Double maxTemperature, WeatherType weatherType, Integer id) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weatherType = weatherType;
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(Double morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public Double getEveningTemperature() {
        return eveningTemperature;
    }

    public void setEveningTemperature(Double eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }

    public Double getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(Double nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DayForecast{" +
                "date=" + date +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", morningTemperature=" + morningTemperature +
                ", eveningTemperature=" + eveningTemperature +
                ", nightTemperature=" + nightTemperature +
                ", weatherType=" + weatherType +
                ", precipitation=" + precipitation +
                ", humidity=" + humidity +
                ", id=" + id +
                '}';
    }
}
