package au.edu.unsw.infs3634.covidtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null){
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());


            //Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }else{
            //launch Main
            launchMainActivity();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode==RESULT_OK) {
           //Launch MainActivity
            launchMainActivity();
        }
    }

    //launch MainActivity method
    public void launchMainActivity(){
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


}