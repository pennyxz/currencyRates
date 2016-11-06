package com.persefone.currencyrates.adapter;

import android.app.Activity;
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

    public ExchangeAdapter(List<Exchange> exchanges, Activity activity) {
        super(activity, 0, exchanges);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_currency_exchanges, null);
        }

        Exchange exchange = getItem(position);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.exchangeDate);
        TextView currentValueTextView = (TextView) convertView.findViewById(R.id.exchangeCurrencyValue);

        String startDateString;
        DateFormat df = new SimpleDateFormat("MMMM, yyyy");
        Date date = exchange.getmDate();
        startDateString = df.format(exchange.getmDate());

        dateTextView.setText(startDateString);
        currentValueTextView.setText(exchange.getmValue().toString());

        return convertView;
    }
}
