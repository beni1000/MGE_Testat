//well this didnt worked as planed..

package ch.mge.calculator.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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


public class ConverterService {
    List<String> keyList;
    private Activity activity;

    public ConverterService(Activity activity){
        this.activity = activity;
    }

    public List<String> getCurrencies() throws IOException {

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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(respMsg);
                            JSONObject rates = obj.getJSONObject("rates");

                            Iterator copyKeys = rates.keys();
                            keyList = new ArrayList<String>();
                            while (copyKeys.hasNext()){
                                String tmp = (String) copyKeys.next();
                                keyList.add(tmp);
                            }
                            Log.d("11111111list", keyList.toString());
                           // Spinner currencySpinner;
                           // currencySpinner = (Spinner)findViewById(activity.R.id.spinner_output);
                           // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_spinner_item, keyList);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        return keyList;
    }
}
