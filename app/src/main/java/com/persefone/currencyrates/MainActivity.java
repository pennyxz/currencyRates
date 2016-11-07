package com.persefone.currencyrates;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.persefone.currencyrates.adapter.CurrencyValueAdapter;
import com.persefone.currencyrates.model.Currency;
import com.persefone.currencyrates.model.Data;
import com.persefone.currencyrates.model.Exchange;
import com.persefone.currencyrates.util.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // URL to get funny JSON
    private static String url = "http://api.fixer.io/latest?base=USD";
    private static String baseUrl = "http://api.fixer.io/";
    public static final String CURRENCY_ID = "currency_id";

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView listView;
    private CurrencyValueAdapter adapter;
    private List<Currency> mCurrencies;
    private List<Exchange> mExchanges;
    private EditText mCurrencyValueToConvert;
    private TextView mCurrencyOnlineTag;
    private Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExchanges = new ArrayList<>();
        data = Data.get(MainActivity.this);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Currency auxCurrency = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, CurrencyDetailActivity.class);
                intent.putExtra(MainActivity.CURRENCY_ID, auxCurrency.getmID());
                startActivity(intent);
            }
        });

        mCurrencyValueToConvert = (EditText) findViewById(R.id.valueToConvert);
        mCurrencyOnlineTag = (TextView) findViewById(R.id.offlineTag);

        new GetCurrencies().execute();


        mCurrencyValueToConvert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!mCurrencyValueToConvert.getText().toString().isEmpty()) {
                    Integer usdAmout = Integer.valueOf(mCurrencyValueToConvert.getText().toString());

                    for (Currency currency : data.getmCurrencies()) {
                        currency.setmValue(data.calculateExchange(usdAmout, currency));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (Currency currency : data.getmCurrencies()) {
                        currency.setmValue(null);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Async task class to get funny json
     */
    private class GetCurrencies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Currencies...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            boolean cantConnect=false;
            HttpHandler sh = new HttpHandler();

            for (int month=0; month<= new Date().getMonth(); month++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.MONTH, month);
                int day= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                //Getting last day of the month. If it's more
                //than the current date, fixer.io will send json for this current date

                String startDateString;
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = calendar.getTime();
                startDateString = df.format(date);

                String url = baseUrl+startDateString+"?base=USD";
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {

                    extractJsonToExchanges(jsonStr);

                } else {
                    cantConnect = true;
                    Log.e(TAG, "Couldn't get json from server.");
                   

                }

            }
            if (cantConnect)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Maybe you are offline, using database values (if any)",
                                Toast.LENGTH_LONG)
                                .show();
                        mCurrencyOnlineTag.setText("Offline");
                        mCurrencyOnlineTag.setTextColor(Color.RED);

                    }
                });
            }

            return null;
        }

        private void extractJsonToExchanges(String jsonStr) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                String startDateString = jsonObj.getString("date");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = df.parse(startDateString);
                    String newDateString = df.format(date);
                } catch (ParseException e) {
                    Log.e(TAG, "Parse Exception");
                }

                JSONObject rates = jsonObj.getJSONObject("rates");
                Double pound = rates.getDouble("GBP");
                Double euro = rates.getDouble("EUR");
                Double yen = rates.getDouble("JPY");
                Double brasil = rates.getDouble("BRL");

                Exchange exchangeUK = new Exchange(101, data.getCurrency("GBP"), pound, date);
                Exchange exchangeEU = new Exchange(100, data.getCurrency("EUR"), euro, date);
                Exchange exchangeJP = new Exchange(102, data.getCurrency("JPY"), yen, date);
                Exchange exchangeBR = new Exchange(103, data.getCurrency("BRL"), brasil, date);

                mExchanges.add(exchangeUK);
                mExchanges.add(exchangeEU);
                mExchanges.add(exchangeJP);
                mExchanges.add(exchangeBR);


            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (mExchanges != null) {
                data.updateDBExchanges(mExchanges);
            }

            mExchanges = new ArrayList<>();
            adapter = new CurrencyValueAdapter(data.getmCurrencies(), MainActivity.this);
            listView.setAdapter(adapter);

        }

    }

}
