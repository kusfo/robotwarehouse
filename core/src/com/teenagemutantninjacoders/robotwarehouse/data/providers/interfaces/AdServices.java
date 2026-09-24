package com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces;

/**
 * Created by JordiM on 02/12/2017.
 */

public interface AdServices {
    void pokeInterstitial();
    boolean mustShowInterstitial();
    void loadInterstitialAd();
    boolean isInterstitialLoaded();
    void showInterstitialAd();
}
