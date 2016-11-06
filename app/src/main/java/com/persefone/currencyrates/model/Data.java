package com.persefone.currencyrates.model;

import android.content.Context;

import com.persefone.currencyrates.util.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by penny on 05/11/16.
 */
public class Data {

    private static Data sData;
    private Context mAppContext;
    private List<Currency> mCurrencies;
    private List<Exchange> mExchanges;
    DBHelper mydb;


    private Data(Context appContext) {
        mAppContext = appContext;
        mydb = new DBHelper(appContext);

        mCurrencies = mydb.getAllCurrencies();
        mExchanges = mydb.getAllExchanges();

    }


    public static Data get(Context c) {
        if (sData == null) {
            sData = new Data(c.getApplicationContext());

        }
        return sData;
    }

    public List<Exchange> getmExchanges() {
        return mExchanges;
    }

    public List<Currency> getmCurrencies() {

        return mCurrencies;
    }

    public List<Currency> getTestCurrencies() {

        Currency currencyUK = new Currency(1, "UK Pounds", "GBP");
        Currency currencyEU = new Currency(2, "EU Euro", "EUR");
        Currency currencyJP = new Currency(3, "Japan Yen", "JPY");
        Currency currencyBR = new Currency(4, "Brazil Reais", "BRL");

        List<Currency> mCurrencies = new ArrayList<>();
        mCurrencies.add(currencyUK);
        mCurrencies.add(currencyEU);
        mCurrencies.add(currencyJP);
        mCurrencies.add(currencyBR);

        return mCurrencies;
    }

    public List<Exchange> getTestExchanges() {

        Date date = new Date();

        Exchange exchangeUK = new Exchange(1, getCurrency("GBP"), 0.80058, date);
        Exchange exchangeEU = new Exchange(2, getCurrency("EUR"), 0.90147, date);
        Exchange exchangeJP = new Exchange(3, getCurrency("JPY"), 102.98, date);
        Exchange exchangeBR = new Exchange(4, getCurrency("BRL"), 3.2449, date);

        List<Exchange> mExchanges = new ArrayList<>();
        mExchanges.add(exchangeUK);
        mExchanges.add(exchangeEU);
        mExchanges.add(exchangeJP);
        mExchanges.add(exchangeBR);

        return mExchanges;
    }

    public void setmCurrencies(List<Currency> mCurrencies) {
        this.mCurrencies = mCurrencies;
    }

    public void setmExchanges(List<Exchange> mExchanges) {
        this.mExchanges = mExchanges;
    }

    public Currency getCurrency(Integer id) {
        for (Currency currency : mCurrencies) {
            if (currency.getmID().equals(id))
                return currency;
        }
        return null;
    }

    public Currency getCurrency(String code) {
        for (Currency currency : mCurrencies) {
            if (currency.getmCode().equals(code))
                return currency;
        }
        return null;
    }

    public Exchange getExchange(Integer id) {
        for (Exchange exchange : mExchanges) {
            if (exchange.getmID().equals(id))
                return exchange;
        }
        return null;
    }

    public Exchange getLatestExchangeRate(Currency currency) {
        for (Exchange exchange : mExchanges) {
            if (exchange.getmCurrency().getmCode().equals(currency.getmCode()))
                return exchange;
        }
        return null;
    }

    public Double calculateExchange(Integer usdValue, Currency currency) {

        if (getLatestExchangeRate(currency) != null)
            return usdValue * getLatestExchangeRate(currency).getmValue();
        else
            return 0.0;
    }

    public List<Exchange> getMonthExchanges(Currency currency) {
        List<Exchange> exchanges = new ArrayList<>();
        for (Exchange exchange : mExchanges) {
            if (exchange.getmCurrency().getmCode().equals(currency.getmCode()))
                exchanges.add(exchange);
        }
        return exchanges;
    }

    public void updateDBExchanges(List<Exchange> mExchanges) {

        for (Exchange exc : mExchanges) {
            Exchange exchangeDB = findExchangeInDB(exc);
            if (exchangeDB != null) {
                if (!exc.getmValue().equals(exc.getmValue())) {
                    exchangeDB.setmValue(exc.getmValue());
                    mydb.updateExchage(exchangeDB);
                }
            } else {
                //Maybe a new month, so add!
                mydb.insertExchange(exc);
            }

        }

        this.mExchanges = mydb.getAllExchanges();

    }

    private Exchange findExchangeInDB(Exchange exc) {
        for (Exchange excDB : mydb.getAllExchanges()) {
            if (exc.getmDate().getMonth() == excDB.getmDate().getMonth() &&
                    exc.getmDate().getYear() == excDB.getmDate().getYear() &&
                    exc.getmCurrency().getmCode().equals(excDB.getmCurrency().getmCode())) {
                return excDB;
            }

        }
        return null;
    }
}
