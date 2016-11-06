package com.persefone.currencyrates.model;

import java.util.Date;

/**
 * Created by penny on 05/11/16.
 */
public class Exchange {

    private Integer mID;
    private Currency mCurrency;
    private Double mValue;
    private Date mDate;

    public Exchange(Integer mID, Currency mCurrency, Double mValue, Date mDate) {
        this.mID = mID;
        this.mCurrency = mCurrency;
        this.mValue = mValue;
        this.mDate = mDate;
    }

    public Exchange() {

    }

    public Integer getmID() {
        return mID;
    }

    public void setmID(Integer mID) {
        this.mID = mID;
    }

    public Currency getmCurrency() {
        return mCurrency;
    }

    public void setmCurrency(Currency mCurrency) {
        this.mCurrency = mCurrency;
    }

    public Double getmValue() {
        return mValue;
    }

    public void setmValue(Double mValue) {
        this.mValue = mValue;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        return getmCurrency() + " Date: " + getmValue() + " Value: " + getmValue();
    }
}
