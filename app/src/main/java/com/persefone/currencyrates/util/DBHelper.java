package com.persefone.currencyrates.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.persefone.currencyrates.model.Currency;
import com.persefone.currencyrates.model.Exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CurrencyRates.db";
    public static final String CURRENCY_TABLE_NAME = "currency";
    public static final String EXCHANGE_TABLE_NAME = "exchange";
    public static final String CURRENCY_COLUMN_ID = "id";
    public static final String CURRENCY_COLUMN_NAME = "name";
    public static final String CURRENCY_COLUMN_CODE = "code";
    public static final String EXCHANGE_COLUMN_ID = "id";
    public static final String EXCHANGE_COLUMN_VALUE = "value";
    public static final String EXCHANGE_COLUMN_DATE = "date";
    public static final String EXCHANGE_COLUMN_CURRENCY = "currency_id";
    private HashMap hp;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE IF NOT EXISTS " + CURRENCY_TABLE_NAME + "("
                + CURRENCY_COLUMN_ID + " integer primary key autoincrement,"
                + CURRENCY_COLUMN_NAME + " varchar, " + CURRENCY_COLUMN_CODE + " varchar);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + EXCHANGE_TABLE_NAME + "("
                + EXCHANGE_COLUMN_ID + " integer primary key autoincrement,"
                + EXCHANGE_COLUMN_VALUE + " real, " + EXCHANGE_COLUMN_DATE + " integer, " + EXCHANGE_COLUMN_CURRENCY + " integer);");

        db.execSQL("INSERT INTO " + CURRENCY_TABLE_NAME + " (" + CURRENCY_COLUMN_NAME + ", " + CURRENCY_COLUMN_CODE + ") VALUES('UK Pounds','GBP');");
        db.execSQL("INSERT INTO " + CURRENCY_TABLE_NAME + " (" + CURRENCY_COLUMN_NAME + ", " + CURRENCY_COLUMN_CODE + ") VALUES('EU Euro','EUR');");
        db.execSQL("INSERT INTO " + CURRENCY_TABLE_NAME + " (" + CURRENCY_COLUMN_NAME + ", " + CURRENCY_COLUMN_CODE + ") VALUES('Japan Yen','JPY');");
        db.execSQL("INSERT INTO " + CURRENCY_TABLE_NAME + "(" + CURRENCY_COLUMN_NAME + ", " + CURRENCY_COLUMN_CODE + ") VALUES('Brazil Reais','BRL');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CURRENCY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCHANGE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertExchange(Exchange exchange) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCHANGE_COLUMN_VALUE, exchange.getmValue());
        contentValues.put(EXCHANGE_COLUMN_DATE, exchange.getmDate().getTime());
        contentValues.put(EXCHANGE_COLUMN_CURRENCY, exchange.getmCurrency().getmID());
        db.insert(EXCHANGE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getDataCurrency(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CURRENCY_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public Cursor getDataExchange(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + EXCHANGE_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRowsCurrency() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CURRENCY_TABLE_NAME);
        return numRows;
    }

    public int numberOfRowsExchange() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EXCHANGE_TABLE_NAME);
        return numRows;
    }

    public boolean updateExchage(Exchange exchange) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCHANGE_COLUMN_DATE, exchange.getmDate().getTime());
        contentValues.put(EXCHANGE_COLUMN_VALUE, exchange.getmValue());
        db.update(EXCHANGE_TABLE_NAME, contentValues, "id = ? ", new String[]{exchange.getmID().toString()});
        return true;
    }


    public List<Exchange> getAllExchanges() {
        List<Exchange> list = new ArrayList<Exchange>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + EXCHANGE_TABLE_NAME + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            list.add(createExchangeObject(res));

            res.moveToNext();
        }
        return list;
    }

    public List<Currency> getAllCurrencies() {
        List<Currency> list = new ArrayList<Currency>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CURRENCY_TABLE_NAME + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            list.add(createCurrencyObject(res));

            res.moveToNext();
        }
        return list;
    }

    private Currency createCurrencyObject(Cursor res) {
        Currency currency = new Currency();

        currency.setmCode(res.getString(res.getColumnIndex(CURRENCY_COLUMN_CODE)));
        currency.setmName(res.getString(res.getColumnIndex(CURRENCY_COLUMN_NAME)));
        currency.setmID((res.getInt(res.getColumnIndex(CURRENCY_COLUMN_ID))));

        return currency;
    }

    private Exchange createExchangeObject(Cursor res) {
        Exchange exchange = new Exchange();

        exchange.setmValue(res.getDouble(res.getColumnIndex(EXCHANGE_COLUMN_VALUE)));
        exchange.setmCurrency(getCurrency(res.getInt(res.getColumnIndex(EXCHANGE_COLUMN_CURRENCY))));
        exchange.setmDate(new Date(res.getLong(res.getColumnIndex(EXCHANGE_COLUMN_DATE))));
        exchange.setmID((res.getInt(res.getColumnIndex(EXCHANGE_COLUMN_ID))));

        return exchange;
    }

    private Currency getCurrency(int id) {
        for (Currency currency : getAllCurrencies()) {
            if (currency.getmID().equals(id)) {
                return currency;
            }
        }
        return null;
    }
}