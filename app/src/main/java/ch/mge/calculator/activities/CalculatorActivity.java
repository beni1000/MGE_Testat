package ch.mge.calculator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;

import com.example.calculator.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import ch.mge.calculator.model.DatabaseHelper;

public class CalculatorActivity extends AppCompatActivity {

    private static final String TAG = "Info";
    private int[] numberBtns = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9};
    private int[] operatorBtns = {R.id.button_divide, R.id.button_multiply, R.id.button_minus, R.id.button_plus};
    private TextView outputScreen;
    private boolean lastNum;
    private boolean lastPoint;
    private boolean errorState;
    DatabaseHelper dbHelper;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        this.setTitle("Calculator");
        this.outputScreen = (TextView) findViewById(R.id.calc_output_screen);

        setNumberOnClickListener();
        setOperatorOnClickListener();

        dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);

        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        }
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

    public void AddData(String newEntry){
        boolean insertData = dbHelper.addData(newEntry);
        Log.d(TAG, "AddData: " + insertData);
        /*if(insertData){
            toastMessage("Insert success!");
        } else {
            toastMessage("Insert failed!");
        }*/

    }



    private void setNumberOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (errorState) {
                    outputScreen.setText(button.getText());
                    errorState = false;
                } else {
                    outputScreen.append(button.getText());
                }
                lastNum = true;
            }
        };
        for (int id : numberBtns) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNum && !errorState) {
                    Button button = (Button) v;
                    if((button.getText()).equals("×")){
                        outputScreen.append("*");
                        lastNum = false;
                        lastPoint = false;
                    }else if ((button.getText()).equals("÷")){
                        outputScreen.append("/");
                        lastNum = false;
                        lastPoint = false;
                    }else{
                        outputScreen.append(button.getText());
                        lastNum = false;
                        lastPoint = false;
                    }
                }
            }
        };
        for (int id : operatorBtns) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.button_point).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNum && !errorState && !lastPoint) {
                    Button button = (Button) v;
                    outputScreen.append(button.getText());
                    lastNum = false;
                    lastPoint = true;
                }
            }
        });

        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputScreen.setText("");
                lastNum = false;
                errorState = false;
                lastPoint = false;
            }
        });
        /*
        findViewById(R.id.button_pl_mn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screen = outputScreen.getText().toString();
                if(!screen.isEmpty() && !errorState){
                    String newLast;
                    if(screen.length() > 1){
                       newLast  = screen.substring(screen.length() - 2, screen.length() - 1);
                    }else {
                        newLast = "";
                    }

                    Log.d("New last logging", "onClick: " + newLast);
                    String rest = screen.substring(0, screen.length() - 1);
                    if(rest.isEmpty()){
                        outputScreen.setText("");
                        lastNum = false;
                        errorState = false;
                        lastPoint = false;
                    }else if (StringUtils.isNumeric(newLast)){
                        outputScreen.setText(rest);
                        lastNum = true;
                        errorState = false;
                        lastPoint = false;
                        Log.d("Teeeestinng", "onClick: nummber deleted ");
                    }else if(newLast.equals("*") || newLast.equals("/") || newLast.equals("+") || newLast.equals("-")){
                        outputScreen.setText(rest);
                        lastNum = false;
                        errorState = false;
                        lastPoint = false;
                    }else if(newLast.equals(".")){
                        outputScreen.setText(rest);
                        lastNum = false;
                        errorState = false;
                        lastPoint = true;
                    }
                }

            }
        });

        findViewById(R.id.button_equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Equals();
            }
        });
    }
    private void Equals(){
        if (lastNum && !errorState) {
            String term = outputScreen.getText().toString();
            Expression expr = new ExpressionBuilder(term).build();
            try {
                double result = expr.evaluate();
                outputScreen.setText(Double.toString(result));
                lastPoint = true;
                String holeLine = term + "= " + result;
                if(holeLine.length() != 0){
                    AddData(holeLine);
                }
            } catch (ArithmeticException ex) {
                outputScreen.setText("ArithmeticException");
                errorState = true;
                lastNum = false;
            }
        }
    }

}
/*private void toastMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }*/