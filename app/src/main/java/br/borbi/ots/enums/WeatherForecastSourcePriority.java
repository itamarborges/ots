package br.borbi.ots.enums;

/**
 * Created by Gabriela on 25/01/2016.
 */
public enum WeatherForecastSourcePriority {

    OPEN_WEATHER,
    DEVELOPER_FORECAST;

    public static WeatherForecastSourcePriority getFirstWeatherForecastSource(){
        return getSource(0);
    }

    public static WeatherForecastSourcePriority getSource(int code){
        if(OPEN_WEATHER.ordinal() == code){
            return OPEN_WEATHER;
        }
        if(DEVELOPER_FORECAST.ordinal() == code){
            return DEVELOPER_FORECAST;
        }
        return OPEN_WEATHER;
    }

    public boolean isOpenWeather(){
        if(this.equals(OPEN_WEATHER)){
            return true;
        }
        return false;
    }

    public boolean isDeveloperForecast(){
        if(this.equals(DEVELOPER_FORECAST)){
            return true;
        }
        return false;
    }
}
