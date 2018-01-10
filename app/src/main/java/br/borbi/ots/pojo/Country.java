package br.borbi.ots.pojo;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by Itamar on 27/09/2015.
 */
public class Country implements Comparable<Country> {

    private int id;
    private String countryCode;
    private String translationFileKey;
    private String countryName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String mCityName) {
        this.countryName = mCityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.countryCode = mCountryCode;
    }

    public String getTranslationFileKay() {
        return translationFileKey;
    }

    public void setTranslationFileKay(String mTranslationFileKey) {
        this.translationFileKey = mTranslationFileKey;
    }

    public Country(int id, String countryCode, String mCityName) {
        this.id = id;
        this.countryCode = countryCode;
        this.countryName = mCityName;
    }

    @Override
    public int compareTo(Country another) {
        Collator cot = Collator.getInstance(new Locale("pt", "BR"));
        cot.setStrength(Collator.PRIMARY);
        return cot.compare(this.getCountryName(), another.getCountryName());
    }

    @Override
    public String toString() {
        return countryName;
    }
}
