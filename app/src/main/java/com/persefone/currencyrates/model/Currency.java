package com.persefone.currencyrates.model;


/**
 * Created by penny on 05/11/16.
 */
public class Currency {

    private Integer mID;
    private String mName;
    private String mCode;
    private Double mValue;

    public Currency(Integer mID, String mName, String mCode) {
        this.mID = mID;
        this.mName = mName;
        this.mCode = mCode;
        this.mValue = null;
    }

    public Currency() {
    }

    public Integer getmID() {
        return mID;
    }

    public void setmID(Integer mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public Double getmValue() {
        return mValue;
    }

    public void setmValue(Double mValue) {
        this.mValue = mValue;
    }

    @Override
    public String toString() {
        return getmName();
    }
}
