package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btnLaunchActivity);
        //button.setText("Text changes");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetailActivity("Test: Message from MainActivity");
                //Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                //intent.putExtra(DetailActivity.INTENT_MESSAGE,  "Test: Message from MainActivity");
                //startActivity(intent);
            }
        });
    }
    private void launchDetailActivity(String message){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.INTENT_MESSAGE,  message);
        startActivity(intent);
    }
}