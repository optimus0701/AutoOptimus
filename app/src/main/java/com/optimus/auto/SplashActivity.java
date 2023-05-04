package com.optimus.auto;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        StartAppSDK.init(this, "209585470", false);
        StartAppAd.disableSplash();
        ImageView imageView = findViewById(R.id.sp_logo_id);
        imageView.setImageResource(R.drawable.splash);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        }, 1000L);
    }
}
