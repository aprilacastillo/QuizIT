package levelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class StartActivity extends AppCompatActivity {
    public static  final String READ = "ok";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRead = prefs.getBoolean(READ,false);

        if (isRead){
            Intent intent = new Intent(StartActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();

        }
    }

    public void ClickStart(View view) {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(READ,true);
        editor.apply();
        startActivity(intent);

    }
}