package com.persefone.currencyrates.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.persefone.currencyrates.R;
import com.persefone.currencyrates.model.Exchange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExchangeAdapter extends ArrayAdapter<Exchange> {
    private Activity mActivity;
    private Exchange lastExchange;

    public ExchangeAdapter(List<Exchange> exchanges, Activity activity, Exchange lastExchange) {
        super(activity, 0, exchanges);
        mActivity = activity;
        this.lastExchange = lastExchange;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_currency_exchanges, null);
        }

        Exchange exchange = getItem(position);


        TextView dateTextView = (TextView) convertView.findViewById(R.id.exchangeDate);
        TextView currentValueTextView = (TextView) convertView.findViewById(R.id.exchangeCurrencyValue);
        TextView currentCode = (TextView) convertView.findViewById(R.id.exchangeCurrencyCode);

        switch (exchange.getmCurrency().getmCode()) {
            case "GBP":
                currentCode.setText("£");
                break;
            case "EUR":
                currentCode.setText("€");
                break;
            case "JPY":
                currentCode.setText("¥");
                break;
            case "BRL":
                currentCode.setText("R$");
                break;
        }

        String startDateString;
        DateFormat df = new SimpleDateFormat("MMMM, yyyy");
        Date date = exchange.getmDate();
        startDateString = df.format(exchange.getmDate());

        dateTextView.setText(startDateString);
        currentValueTextView.setText(exchange.getmValue().toString());

        if (lastExchange.getmValue() > exchange.getmValue()) {
            currentValueTextView.setTextColor(Color.RED);
        } else if (lastExchange.getmValue() < exchange.getmValue()) {
            currentValueTextView.setTextColor(Color.GREEN);
        } else {
            currentValueTextView.setTextColor(Color.GRAY);
        }

        return convertView;
    }
}
