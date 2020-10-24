package ch.mge.calculator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calculator.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorActivity extends AppCompatActivity {

    private int[] numberBtns = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9};
    private int[] operatorBtns = {R.id.button_divide, R.id.button_multiply, R.id.button_minus, R.id.button_plus};
    private TextView outputScreen;
    private boolean lastNum;
    private boolean lastDot;
    private boolean errorState;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        this.setTitle("Calculator");
        this.outputScreen = (TextView) findViewById(R.id.calc_output_screen);
        setNumberOnClickListener();
        setOperatorOnClickListener();
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    outputScreen.append(button.getText());
                    lastNum = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorBtns) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.button_point).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNum && !errorState && !lastDot) {
                    Button button = (Button) v;
                    outputScreen.append(button.getText());
                    lastNum = false;
                    lastDot = true;
                }
            }
        });

        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputScreen.setText("");
                lastNum = false;
                errorState = false;
                lastDot = false;
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
            // Read the expression
            String txt = outputScreen.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                outputScreen.setText(Double.toString(result));
                lastDot = true; // Result contains a dot
            } catch (ArithmeticException ex) {
                // Display an error message
                outputScreen.setText("Error");
                errorState = true;
                lastNum = false;
            }
        }
    }

}