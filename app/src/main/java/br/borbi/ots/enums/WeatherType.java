package br.borbi.ots.enums;

/**
 * Created by Gabriela on 28/05/2015.
 */
public enum WeatherType {

    SUNNY,
    RAIN,
    CLOUDS,
    SNOW;

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


    public boolean isSunnyDay(boolean acceptsClouds){
        if(this.equals(SUNNY) || (acceptsClouds && this.equals(CLOUDS))){
            return true;
        }

        return false;

    }
}
