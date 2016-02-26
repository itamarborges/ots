package br.borbi.ots.enums;

/**
 * Created by Gabriela on 28/05/2015.
 */
public enum WeatherType {

    SUNNY,
    RAIN,
    CLOUDS,
    SNOW;

    private static final String DEVELOPER_FORECAST_CLEAR_DAY = "clear-day";
    private static final String DEVELOPER_FORECAST_CLEAR_NIGHT = "clear-night";
    private static final String DEVELOPER_FORECAST_RAIN = "rain";
    private static final String DEVELOPER_FORECAST_SNOW = "snow";
    private static final String DEVELOPER_FORECAST_SLEET = "sleet";
    private static final String DEVELOPER_FORECAST_CLOUDY = "cloudy";
    private static final String DEVELOPER_FORECAST_PARTLY_CLOUDY_DAY = "partly-cloudy-day";
    private static final String DEVELOPER_FORECAST_PARTLY_CLOUDY_NIGHT = "partly-cloudy-night";


    public static WeatherType getWeatherType(int weatherType){

        if(weatherType == 800 || weatherType == 801){
            return  WeatherType.SUNNY;
        }

        if(weatherType >= 802 && weatherType <= 804){
            return WeatherType.CLOUDS;
        }

        if(weatherType > 600 && weatherType < 700){
            return WeatherType.SNOW;
        }

        return WeatherType.RAIN;
    }

    public static WeatherType getWeatherTypeByDeveloperForecast(String icon){
        //wind, fog
        if(DEVELOPER_FORECAST_SNOW.equals(icon) || DEVELOPER_FORECAST_SLEET.equals(icon)){
            return WeatherType.SNOW;
        }

        if(DEVELOPER_FORECAST_RAIN.equals(icon)){
            return WeatherType.RAIN;
        }

        if(DEVELOPER_FORECAST_CLEAR_DAY.equals(icon)){
            return WeatherType.SUNNY;
        }

        return WeatherType.CLOUDS;
    }

    public static int getId(WeatherType weatherType){
        if(WeatherType.SUNNY.equals(weatherType)){
            return 800;
        }
        if(WeatherType.CLOUDS.equals(weatherType)){
            return 802;
        }
        if(WeatherType.SNOW.equals(weatherType)){
            return 600;
        }
        return 500;
    }


    public boolean isSunnyDay(boolean acceptsClouds){
        if(this.equals(SUNNY) || (acceptsClouds && this.equals(CLOUDS))){
            return true;
        }

        return false;

    }
}
