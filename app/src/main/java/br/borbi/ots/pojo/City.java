package br.borbi.ots.pojo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gabriela on 28/05/2015.
 */
public class City implements Serializable{

    private static final long serialVersionUID = 42L;

    private int id;
    private Double latitude;
    private Double longitude;
    private String nameEnglish;
    private int countryId;
    private String translationFileKey;
    //Name in the mobile's language
    private String name;

    private String countryCode;
    private String countryName;
    private List<DayForecast> dayForecasts;
    private List<String> tags;

    public City(int id, Double latitude, Double longitude, String nameEnglish, int countryId, String translationFileKey) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nameEnglish = nameEnglish;
        this.countryId = countryId;
        this.translationFileKey = translationFileKey;
    }

    public City(int id, String nameEnglish, String countryCode, List<DayForecast> dayForecasts) {
        this.id = id;
        this.nameEnglish = nameEnglish;
        this.countryCode = countryCode;
        this.dayForecasts = dayForecasts;
    }

    public City(int id, String nameEnglish, String countryCode) {
        this.id = id;
        this.nameEnglish = nameEnglish;
        this.countryCode = countryCode;
    }

    public City(int id, String nameEnglish, String countryCode, String countryName, Double latitude, Double longitude) {
        this.id = id;
        this.nameEnglish = nameEnglish;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public City() { }

    public String getName() { return name; }

    public void setName(String name) { this.name = name;}

    public int getCountryId() { return countryId;}

    public void setCountryId(int countryId) { this.countryId = countryId;}

    public String getNameEnglish() { return nameEnglish; }

    public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }

    public List<DayForecast> getDayForecasts() {
        return dayForecasts;
    }

    public void setDayForecasts(List<DayForecast> dayForecasts) { this.dayForecasts = dayForecasts; }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

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

    public List<String> getTags() { return tags; }

    public void setTags(List<String> tags) {this.tags = tags; }

    public String getTranslationFileKey() { return translationFileKey; }

    public void setTranslationFileKey(String translationFileKey) { this.translationFileKey = translationFileKey;}

    @Override
    public String toString() {

        String retorno = "City{" +
                "id=" + id +
                ", nameEnglish='" + nameEnglish + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", name='" + name + '\'' +
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
