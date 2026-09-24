package com.teenagemutantninjacoders.robotwarehouse.android.ads;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.teenagemutantninjacoders.robotwarehouse.R;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.AdServices;



/**
 * Created by JordiM on 21/12/2017.
 */

public class AndroidAdHandler implements AdServices {
    private static final String AD_MOB_LAPSE_CICLE = "admoblapsecycle";
    private static final String AD_MOB_LAPSE_CICLE_VALUE = "admob_lapse_cycle";
    private Activity activity;
    private InterstitialAd interstitialAd;
    private FullScreenContentCallback fullScreenContentCallback;
    private boolean mustShowAd;
    private boolean intestitialLoaded;


    public AndroidAdHandler(Activity activity) {
        this.activity = activity;
    }

    public void initialize(FullScreenContentCallback fullScreenContentCallback) {
        this.fullScreenContentCallback = fullScreenContentCallback;
        mustShowAd = false;
        intestitialLoaded = false;
    }

    @Override
    public void pokeInterstitial() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        long adMobCycle = preferences.getLong(AD_MOB_LAPSE_CICLE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (adMobCycle == 0) {
            editor.putLong(AD_MOB_LAPSE_CICLE, FirebaseRemoteConfig.getInstance().getLong(AD_MOB_LAPSE_CICLE_VALUE));
            mustShowAd = true;
        } else {
            adMobCycle--;
            editor.putLong(AD_MOB_LAPSE_CICLE, adMobCycle);
        }
        editor.apply();
    }

    @Override
    public boolean mustShowInterstitial() {
        //return mustShowAd;
        return true;
    }

    @Override
    public void loadInterstitialAd() {
        Log.i("Ad Handler", "Starting request for load interstitial Ad");
        intestitialLoaded = false;
        final AdRequest adRequest = new AdRequest.Builder().build();
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    InterstitialAd.load(activity, activity.getString(R.string.firebase_admob_interstitial_id), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd newinterstitialAd) {
                            interstitialAd = newinterstitialAd;
                            intestitialLoaded = true;
                            interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            interstitialAd = null;
                            intestitialLoaded = false;
                            String error =
                                    String.format(
                                            "domain: %s, code: %d, message: %s",
                                            loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                            Log.i("Ad Handler", error);
                        }
                    });
                }
            });
        } catch (Exception ex) {
            Log.i("Android Launcher", "Exception loading Interstitial Ad: " + ex.getMessage());
            ex.printStackTrace();
            interstitialAd = null;
            intestitialLoaded = false;
        }
    }

    @Override
    public boolean isInterstitialLoaded() {
        return intestitialLoaded;
    }

    @Override
    public void showInterstitialAd() {
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (intestitialLoaded && interstitialAd != null) {
                        interstitialAd.show(activity);
                        mustShowAd = false;

                    } else {
                        Log.i("Ad Handler", "Interstitial Ad not loaded");
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("Android Ad Handler", "Exception running Interstitial Ad: " + ex.getMessage());
            ex.printStackTrace();
            mustShowAd = false;
        }
    }
}
