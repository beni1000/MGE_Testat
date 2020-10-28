package ch.mge.calculator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calculator.R;

public class PythagorasActivity extends AppCompatActivity {
    private EditText a;
    private EditText b;
    private EditText c;
    private Button btnCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pythagoras);
        this.setTitle("Pythagoras");

        btnCalc = (Button)findViewById(R.id.btn_pyth_calc);
        btnCalc.setEnabled(false);

        a = (EditText)findViewById(R.id.editText_a);
        b = (EditText)findViewById(R.id.editText_b);
        c = (EditText)findViewById(R.id.editText_c);

        setCalcOnClickListener(btnCalc);

        a.addTextChangedListener(calcWatcher);
        b.addTextChangedListener(calcWatcher);
        c.addTextChangedListener(calcWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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
            case R.id.action_darkmode:
                SharedPreferences sharedPreferences
                        = getSharedPreferences(
                        "sharedPrefs", MODE_PRIVATE);
                boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
                if (isDarkModeOn) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TextWatcher calcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                String a_in = a.getText().toString().trim();
                String b_in = b.getText().toString().trim();
                String c_in = c.getText().toString().trim();

                if (!a_in.isEmpty() && !b_in.isEmpty() && c_in.isEmpty()){
                    btnCalc.setEnabled(true);
                }else if (!a_in.isEmpty() && b_in.isEmpty() && !c_in.isEmpty()){
                    btnCalc.setEnabled(true);
                }else if(a_in.isEmpty() && !b_in.isEmpty() && !c_in.isEmpty()){
                    btnCalc.setEnabled(true);
                }else {
                    btnCalc.setEnabled(false);
                }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void setCalcOnClickListener(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("chraaaashhh", "why");
                String a_in = a.getText().toString();
                //Log.d("aaaaaaaaaaaaaaaaaaaaa", a_in);
                String b_in = b.getText().toString();
                String c_in = c.getText().toString();
                if (a_in.isEmpty()){
                    double b_val = Double.parseDouble(b_in);
                    double c_val = Double.parseDouble(c_in);
                    double result = Math.sqrt(Math.pow(c_val, 2) - Math.pow(b_val, 2));
                    a.setText(String.valueOf(result));
                }else if(b_in.isEmpty()){
                    double a_val = Double.parseDouble(a_in);
                    double c_val = Double.parseDouble(c_in);
                    double result = Math.sqrt(Math.pow(c_val, 2) - Math.pow(a_val, 2));
                    b.setText(String.valueOf(result));
                }else {
                    double a_val = Double.parseDouble(a_in);
                    double b_val = Double.parseDouble(b_in);
                    double result = Math.sqrt((a_val * a_val) + (b_val * b_val));
                    Log.d("Testing 0000007", String.valueOf(result));
                    c.setText(String.valueOf(result));
                }
            }
        });
    }
}
