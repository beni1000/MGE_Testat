package ch.mge.calculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import ch.mge.calculator.services.ConverterService;

public class ExchangeActivity extends AppCompatActivity {
    List<String> spinnerList;
    Spinner currencySpinner;
    TextView outputView;
    //ConverterService converterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        this.setTitle("Exchange rates");
        //converterService = new ConverterService(ExchangeActivity.this);

        currencySpinner = (Spinner)findViewById(R.id.spinner_output);
        final EditText inputCash = (EditText)findViewById(R.id.txt_cash_input);
        outputView = (TextView)findViewById(R.id.txt_cash_output);
        final Button btnCalc = (Button)findViewById(R.id.btn_calc_cash);

        try{
            getCurrencies();
            //spinnerList = converterService.getCurrencies();
            //ArrayAdapter<String> spinArrAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);
            //currencySpinner.setAdapter(spinArrAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setCalcOnClickListener(btnCalc, inputCash);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculator:
                Intent calcIntent = new Intent(this, CalculatorActivity.class);
                this.startActivity(calcIntent);
                return true;
            case R.id.action_history:
                Intent historyIntent = new Intent(this, HistoryActivity.class);
                this.startActivity(historyIntent);
                return true;
            case R.id.action_exchange:
                Intent exchangeIntent = new Intent(this, ExchangeActivity.class);
                this.startActivity(exchangeIntent);
                return true;
            case R.id.action_pythagoras:
                Intent pythagorasIntent = new Intent(this, PythagorasActivity.class);
                this.startActivity(pythagorasIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCalcOnClickListener(Button btn, final EditText input){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!input.getText().toString().isEmpty()){
                        String to = currencySpinner.getSelectedItem().toString();
                        double inputV = Double.valueOf(input.getText().toString());
                        try{
                            calcExchange(to, inputV);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(ExchangeActivity.this, "Enter Value!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void getCurrencies() throws IOException {

        String link = "https://api.exchangeratesapi.io/latest";
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(link)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w("failed to load:", e.getMessage().toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String respMsg = response.body().string();
                ExchangeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(respMsg);
                            JSONObject rates = obj.getJSONObject("rates");

                            Iterator copyKeys = rates.keys();
                            spinnerList = new ArrayList<String>();
                            while (copyKeys.hasNext()){
                                String tmp = (String) copyKeys.next();
                                spinnerList.add(tmp);
                            }
                            Log.d("Liste inhalt: ", spinnerList.toString());
                            currencySpinner = (Spinner)findViewById(R.id.spinner_output);
                            ArrayAdapter<String> spinArrAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);
                            currencySpinner.setAdapter(spinArrAdapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void calcExchange(final String to, final double input) throws IOException{
        String link = "https://api.exchangeratesapi.io/latest";
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(link)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w("Response:", e.getMessage().toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String resp = response.body().string();
                Log.d("waas git das us:", resp);
                ExchangeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject objAll = new JSONObject(resp);
                            JSONObject x = objAll.getJSONObject("rates");

                            String toC = x.getString(to);
                            String chf = x.getString("CHF");

                            double result = input / Double.valueOf(chf) * Double.valueOf(toC);
                            outputView.setText(String.valueOf(result));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
