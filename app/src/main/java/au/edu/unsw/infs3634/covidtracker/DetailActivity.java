package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_Message";
    private String TAG = "DETAIL_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Line 18");
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: Line 21");
        //Intent intent = getIntent();
        TextView detailMessage = findViewById(R.id.tvDetailMessage);
        detailMessage.setText(intent.getStringExtra(INTENT_MESSAGE));

        Button button = findViewById(R.id.btnShowVideo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutube();
            }
        });
    }

    public void openYoutube() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Tlwda9s58Lg&ab_channel=Fireballfury"));
    startActivity(intent);}
}