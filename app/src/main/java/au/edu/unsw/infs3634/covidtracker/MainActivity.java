package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
private RecyclerView mRecyclerView;
private CountryAdapter mAdapter;
private RecyclerView.LayoutManager LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        CountryAdapter.Listener listener = new CountryAdapter.Listener() {
            @Override
            public void onClick(View view, String countryCode) {
                launchDetailActivity(countryCode);
            }
        };
        
        mAdapter = new CountryAdapter(Country.getCountries(), listener);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void launchDetailActivity(String message) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.INTENT_MESSAGE, message);
        startActivity(intent);
    }
}