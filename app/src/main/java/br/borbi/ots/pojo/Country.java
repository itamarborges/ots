package br.borbi.ots.pojo;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by Itamar on 27/09/2015.
 */
public class Country implements Comparable<Country> {

    private int mId;
    private String mCountryCode;
    private String mTranslationFileKey;
    private String mCountryName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String mCityName) {
        this.mCountryName = mCityName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getTranslationFileKay() {
        return mTranslationFileKey;
    }

    public void setTranslationFileKay(String mTranslationFileKey) {
        this.mTranslationFileKey = mTranslationFileKey;
    }

    public Country(int mId, String mCountryCode, String mCityName) {
        this.mId = mId;
        this.mCountryCode = mCountryCode;
        this.mCountryName = mCityName;
    }

    @Override
    public int compareTo(Country another) {
        Collator cot = Collator.getInstance(new Locale("pt", "BR"));
        cot.setStrength(Collator.PRIMARY);
        return cot.compare(this.getCountryName(), another.getCountryName());
    }

    @Override
    public String toString() {
        return mCountryName;
    }
}
