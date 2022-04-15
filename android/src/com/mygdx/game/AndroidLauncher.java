package com.mygdx.game;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mygdx.game.adMod.AdAds;


public class AndroidLauncher extends AndroidApplication implements AdAds {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            // The app has the camera permission.
//            Log.v("ERROR", "CAMERA YESSS !!!!!!");
//        } else {
//            // Apply for the camera permission.
//            Log.v("ERROR", "CAMERA NOOO !!!!!!!!!!!!!! ");
//        }


        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useGyroscope = false;
        initialize(new ZombiKiller(1,this), config);
        ////////////////////////////////////////////////////
        MobileAds.initialize(this, "ca-app-pub-3062739183422189~2319030096");
        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3062739183422189/6323969964");
        mInterstitialAd.setAdUnitId("ca-app-pub-3062739183422189/1599925199"); // ta samaya reklama
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/5224354917"); // test
        //ca-app-pub-3940256099942544/6300978111
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        ////////////////////////////////////////////////////
    }

    @Override
    public void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Gdx.app.log("ANDROID", "The interstitial wasn't loaded yet.");
                }
            }
        });

    }
    //implements AdAds
}
