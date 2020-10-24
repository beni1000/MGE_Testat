package ch.mge.calculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setTitle("History");
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
}
