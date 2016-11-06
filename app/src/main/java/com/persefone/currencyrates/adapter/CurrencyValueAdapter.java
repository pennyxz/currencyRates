package com.persefone.currencyrates.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.persefone.currencyrates.R;
import com.persefone.currencyrates.model.Currency;

import java.util.List;

public class CurrencyValueAdapter extends ArrayAdapter<Currency> {
    private Activity mActivity;

    public CurrencyValueAdapter(List<Currency> crimes, Activity activity) {
        super(activity, 0, crimes);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_currency_main, null);
        }

        Currency currency = getItem(position);

        TextView currentValueTextView = (TextView) convertView.findViewById(R.id.currencyValue);
        TextView currencyCodeTextView = (TextView) convertView.findViewById(R.id.currencyCode);
        TextView currencySymbolTextView = (TextView) convertView.findViewById(R.id.currencySymbol);


        if (currency.getmValue() != null) {
            currentValueTextView.setText(currency.getmValue().toString());
        } else {
            currentValueTextView.setText("- No Data -");
        }

        currencyCodeTextView.setText(currency.getmCode());

        switch (currency.getmCode()) {
            case "GBP":
                currencySymbolTextView.setText("£");
                break;
            case "EUR":
                currencySymbolTextView.setText("€");
                break;
            case "JPY":
                currencySymbolTextView.setText("¥");
                break;
            case "BRL":
                currencySymbolTextView.setText("R$");
                break;
        }


        return convertView;
    }
}
