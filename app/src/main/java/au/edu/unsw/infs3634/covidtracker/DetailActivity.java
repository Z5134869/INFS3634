package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";
    private TextView mCountry;
    private TextView mNewCases;
    private TextView mTotalCases;
    private TextView mNewDeaths;
    private TextView mTotalDeaths;
    private TextView mNewRecovered;
    private TextView mTotalRecovered;
    private ImageView mSearch;
    private ImageView mFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String countryCode = intent.getStringExtra(INTENT_MESSAGE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidService service = retrofit.create(CovidService.class);
        Call<Response> responseCall = service.getResponse();
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d(TAG, "onResponse: API call succeeded!");
                List<Country> countries = response.body().getCountries();
                final Country country = Country.findCountry(countries, countryCode);

                mCountry = findViewById(R.id.tvCountryName);
                mNewCases = findViewById(R.id.tvNewCasesDesc);
                mTotalCases = findViewById(R.id.tvTotalCasesDesc);
                mNewDeaths = findViewById(R.id.tvNewDeathsDesc);
                mTotalDeaths = findViewById(R.id.tvTotalDeathsDesc);
                mNewRecovered = findViewById(R.id.tvNewRecoveredDesc);
                mTotalRecovered = findViewById(R.id.tvTotalRecoveredDesc);

                mCountry.setText(country.getCountry());
                mNewCases.setText(String.valueOf(country.getNewConfirmed()));
                mTotalCases.setText(String.valueOf(country.getTotalConfirmed()));
                mNewDeaths.setText(String.valueOf(country.getNewDeaths()));
                mTotalDeaths.setText(String.valueOf(country.getTotalDeaths()));
                mNewRecovered.setText(String.valueOf(country.getNewRecovered()));
                mTotalRecovered.setText(String.valueOf(country.getTotalRecovered()));

                mSearch = findViewById(R.id.ivSearch);
                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.google.com/search?q=covid "
                                        + country.getCountry()));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: API call failed.");
            }
        });

        mFlag = findViewById(R.id.ivFlag);
        Glide.with(this).load("https://www.countryflags.io/"+ countryCode + "/flat/64.png").into(mFlag);



//        TextView tv = findViewById(R.id.tvDetailMessage);
//        tv.setText(country.getCountry());
//        // TextView detailMessage = findViewById(R.id.tvDetailMessage);
//        // detailMessage.setText(intent.getStringExtra(INTENT_MESSAGE));
//
//        Button button = findViewById(R.id.btnLink);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/"));
//                startActivity(intent);
//            }
//        });

    }
}