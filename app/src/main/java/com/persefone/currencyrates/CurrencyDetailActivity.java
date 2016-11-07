package com.persefone.currencyrates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.persefone.currencyrates.adapter.ExchangeAdapter;
import com.persefone.currencyrates.model.Currency;
import com.persefone.currencyrates.model.Data;
import com.persefone.currencyrates.model.Exchange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CurrencyDetailActivity extends AppCompatActivity {


    private String TAG = CurrencyDetailActivity.class.getSimpleName();
    private ListView listView;

    public static final String CURRENCY_ID = "currency_id";

    private Currency mCurrency;
    private Exchange mLastExchange;
    private List<Exchange> mMonthsExchanges;
    private TextView mCurrencyValueText;
    private TextView mCurrencyCode;
    private TextView mCurrencyLastUpdate;
    private TextView mCurrencySymbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detail);


        listView = (ListView) findViewById(R.id.listHist);

        Integer currencyID = (Integer) getIntent().getExtras().getSerializable(CURRENCY_ID);

        mCurrency = Data.get(CurrencyDetailActivity.this).getCurrency(currencyID);
        mLastExchange = Data.get(CurrencyDetailActivity.this).getLatestExchangeRate(mCurrency);

        if (mLastExchange != null) {

            setTitle(mCurrency.getmName() + " Currency Details");

            mMonthsExchanges = Data.get(CurrencyDetailActivity.this).getMonthExchanges(mCurrency);

            ExchangeAdapter adapter = new ExchangeAdapter(mMonthsExchanges, CurrencyDetailActivity.this, mLastExchange);

            listView.setAdapter(adapter);

            mCurrencyValueText = (TextView) findViewById(R.id.valueCurrency);
            mCurrencyCode = (TextView) findViewById(R.id.codeCurrency);
            mCurrencySymbol = (TextView) findViewById(R.id.currencySymbolDetail);

            String startDateString;
            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
            startDateString = df.format(mLastExchange.getmDate());

            mCurrencyLastUpdate = (TextView) findViewById(R.id.lastUpdateValue);

            mCurrencyValueText.setText(mLastExchange.getmValue().toString());
            mCurrencyCode.setText(mCurrency.getmCode());
            mCurrencyLastUpdate.setText(startDateString);

            switch (mCurrency.getmCode()) {
                case "GBP":
                    mCurrencySymbol.setText("£");
                    break;
                case "EUR":
                    mCurrencySymbol.setText("€");
                    break;
                case "JPY":
                    mCurrencySymbol.setText("¥");
                    break;
                case "BRL":
                    mCurrencySymbol.setText("R$");
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "There is no Exchange data on Database. Try getting online once",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }
}
