package br.borbi.ots.pojo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gabriela on 28/05/2015.
 */
public class City implements Serializable{

    private static final long serialVersionUID = 42L;

    private String name;
    private List<DayForecast> dayForecasts;

    public City(String name, List<DayForecast> dayForecasts) {
        this.name = name;
        this.dayForecasts = dayForecasts;
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

    @Override
    public String toString() {

        String retorno = "City{" +
                "name='" + name + '\'' +
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
