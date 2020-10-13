package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        CountryAdapter.RecyclerViewClickListener listener = new CountryAdapter.RecyclerViewClickListener() {
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

    @Override
    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                mAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.sort_new:
                mAdapter.sort(1);
                return true;
            case R.id.sort_total:
                mAdapter.sort(2);
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}