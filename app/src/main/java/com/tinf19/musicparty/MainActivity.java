package com.tinf19.musicparty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.tinf19.musicparty.client.JoinActivity;
import com.tinf19.musicparty.databinding.ActivityMainBinding;
import com.tinf19.musicparty.server.HostActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tinf19.musicparty.util.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;



    //Android lifecycle methods

    @Override
    protected void onStart() {
        super.onStart();
        ImageView imageView = findViewById(R.id.animatedLogo);
        Log.d(TAG, "animate App logo at the header");
        animate(imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.createPartyCardView.setEnabled(true);
        binding.joinPartyCardView.setEnabled(true);

        SharedPreferences firstConnection = this.getSharedPreferences("firstConnection", Context.MODE_PRIVATE);
        if(firstConnection.getBoolean(Constants.FIRST_CONNECTION, true)) {
            Log.d(TAG, "user connected for the first time ");
            showInfoText(null);
            SharedPreferences.Editor editor = firstConnection.edit();
            editor.putBoolean(Constants.FIRST_CONNECTION, false);
            editor.apply();
        }
    }

    public void animate(ImageView v) {
        Drawable d = v.getDrawable();
        if(d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        } else if(d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        }
    }

    public void changeHost(View view){
        Log.d(TAG, "starting HostActivity");
        startActivity(new Intent(this, HostActivity.class));
    }

    public void changeClient(View view){
        Log.d(TAG, "starting ClientLoginActivity");
        startActivity(new Intent(this, JoinActivity.class));
    }

    public void showInfoText(View view) {
        Log.d(TAG, "info text is visible");
        binding.infoCardView.setVisibility(View.VISIBLE);
    }

    public void hideInfoText(View view) {
        Log.d(TAG, "info field is invisible");
        binding.infoCardView.setVisibility(View.INVISIBLE);
    }


}