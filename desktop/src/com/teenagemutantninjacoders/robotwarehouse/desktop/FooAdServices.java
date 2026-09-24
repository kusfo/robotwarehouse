package com.teenagemutantninjacoders.robotwarehouse.desktop;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.AdServices;

/**
 * Created by JordiM on 02/12/2017.
 */

class FooAdServices implements AdServices {
    @Override
    public void pokeInterstitial() {

    }

    @Override
    public boolean mustShowInterstitial() {
        return false;
    }

    @Override
    public void loadInterstitialAd() {

    }

    @Override
    public boolean isInterstitialLoaded() {
        return false;
    }

    @Override
    public void showInterstitialAd() {

    }
}
