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
    private WeatherType weatherType;
    private Double precipitation;
    private Double humidity;

    public DayForecast(Date date, Double minTemperature, Double maxTemperature, WeatherType weatherType, Double precipitation, Double humidity) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weatherType = weatherType;
        this.precipitation = precipitation;
        this.humidity = humidity;
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

    @Override
    public String toString() {
        return "DayForecast{" +
                "date=" + date +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", weatherType=" + weatherType +
                ", precipitation=" + precipitation +
                ", humidity=" + humidity +
                '}';
    }
}
