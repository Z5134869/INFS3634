package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private CountryDatabase mDb;
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
    private CheckBox mHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCountry = findViewById(R.id.tvCountryName);
        mNewCases = findViewById(R.id.tvNewCasesDesc);
        mTotalCases = findViewById(R.id.tvTotalCasesDesc);
        mNewDeaths = findViewById(R.id.tvNewDeathsDesc);
        mTotalDeaths = findViewById(R.id.tvTotalDeathsDesc);
        mNewRecovered = findViewById(R.id.tvNewRecoveredDesc);
        mTotalRecovered = findViewById(R.id.tvTotalRecoveredDesc);
        mFlag = findViewById(R.id.ivFlag);
        mHome = findViewById(R.id.cbHome);


        Intent intent = getIntent();
        String countryCode = intent.getStringExtra(INTENT_MESSAGE);

        // create a Room database
        mDb = Room.databaseBuilder(getApplicationContext(), CountryDatabase.class, "country-database").build();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Country country = mDb.countryDao().getCountry(countryCode);
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
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference messageRef = database.getReference(FirebaseAuth.getInstance().getUid());
                messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String result = (String) dataSnapshot.getValue();
                        if (result != null && result.equals(country.getCountryCode())) {
                            mHome.setChecked(true);
                        } else {
                            mHome.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DatabaseReference messageRef = database.getReference(FirebaseAuth.getInstance().getUid());
                        if (isChecked) {
                            messageRef.setValue(country.getCountryCode());
                        } else {
                            messageRef.setValue("");
                        }
                    }
                });
            }
        });

        Glide.with(this).load("https://www.countryflags.io/"+ countryCode + "/flat/64.png").into(mFlag);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.covid19api.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        CovidService service = retrofit.create(CovidService.class);
//        Call<Response> responseCall = service.getResponse();
//        responseCall.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                Log.d(TAG, "onResponse: API call succeeded!");
//                List<Country> countries = response.body().getCountries();
//                final Country country = Country.findCountry(countries, countryCode);
//
//                mCountry = findViewById(R.id.tvCountryName);
//                mNewCases = findViewById(R.id.tvNewCasesDesc);
//                mTotalCases = findViewById(R.id.tvTotalCasesDesc);
//                mNewDeaths = findViewById(R.id.tvNewDeathsDesc);
//                mTotalDeaths = findViewById(R.id.tvTotalDeathsDesc);
//                mNewRecovered = findViewById(R.id.tvNewRecoveredDesc);
//                mTotalRecovered = findViewById(R.id.tvTotalRecoveredDesc);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                Log.d(TAG, "onFailure: API call failed.");
//            }
//        });





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