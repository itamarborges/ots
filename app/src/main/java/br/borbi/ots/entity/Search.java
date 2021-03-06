package br.borbi.ots.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.borbi.ots.pojo.CityResultSearch;

/**
 * Created by Gabriela on 08/06/2015.
 */
public class Search implements Serializable{

    private Long id;
    private Date beginDate;
    private Date endDate;
    private Integer radius;
    private Integer minSunnyDays;
    private Boolean includesCloudyDays;
    private Double minTemperature;
    private Date dateTimeLastSearch;
    private Double originLatitude;
    private Double originLongitude;
    private List<CityResultSearch> cites;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getMinSunnyDays() {
        return minSunnyDays;
    }

    public void setMinSunnyDays(Integer minSunnyDays) {
        this.minSunnyDays = minSunnyDays;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Date getDateTimeLastSearch() {
        return dateTimeLastSearch;
    }

    public void setDateTimeLastSearch(Date dateTimeLastSearch) {
        this.dateTimeLastSearch = dateTimeLastSearch;
    }

    public List<CityResultSearch> getCites() {
        return cites;
    }

    public void setCites(List<CityResultSearch> cites) {
        this.cites = cites;
    }

    public Double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public Double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public Boolean getIncludesCloudyDays() {
        return includesCloudyDays;
    }

    public void setIncludesCloudyDays(Boolean includesCloudyDays) {
        this.includesCloudyDays = includesCloudyDays;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", radius=" + radius +
                ", minSunnyDays=" + minSunnyDays +
                ", includesCloudyDays=" + includesCloudyDays +
                ", minTemperature=" + minTemperature +
                ", dateTimeLastSearch=" + dateTimeLastSearch +
                ", originLatitude=" + originLatitude +
                ", originLongitude=" + originLongitude +
                ", cites=" + cites +
                '}';
    }
}
