package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CountryAdapter mAdapter;
    private CountryDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        CountryAdapter.Listener listener = new CountryAdapter.Listener() {
            @Override
            public void onClick(View view, String conutryCode) {
                launchDetailActivity(conutryCode);
            }
        };

        mAdapter = new CountryAdapter(new ArrayList<Country>(), listener, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        // create a Room database
        mDb = Room.databaseBuilder(getApplicationContext(), CountryDatabase.class, "country-database").build();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(mDb.countryDao().getCountries());
                mAdapter.sort(2);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidService service = retrofit.create(CovidService.class);

        Call<Response> responseCall = service.getResponse();
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d(TAG, "onRsponse: API call succeeded!");
                List<Country> countries = response.body().getCountries();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.countryDao().deleteAll(mDb.countryDao().getCountries().toArray(new Country[0]));
                        mDb.countryDao().insertAll(countries.toArray(new Country[0]));
                    }
                });
                mAdapter.setData(countries);
                mAdapter.sort(2);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String result = (String) snapshot.getValue();
                        if(result !=null){
                            for(Country home: countries){
                                if(home.getCountryCode().equals(result)){
                                    //display toast message
                                    Toast.makeText(MainActivity.this,home.getNewConfirmed()+" new cases in "+home.getCountry(),Toast.LENGTH_LONG).show();
                                }
                            };
                        }else{
                         
                        };
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: API call failed.");
            }
        });
        // use Gson library to convert json data to java class
//        Gson gson = new Gson();
//        Response response = gson.fromJson(Response.json, Response.class);
    }

    private void launchDetailActivity(String message) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.INTENT_MESSAGE, message);
        startActivity(intent);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_new:
                mAdapter.sort(1);
            return true;
            case R.id.sort_total:
                mAdapter.sort(2);
                default:
                return super.onOptionsItemSelected(item);
        }
    }
}