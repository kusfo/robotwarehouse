package com.teenagemutantninjacoders.robotwarehouse.desktop;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;

public class FooPlatformServices implements PlatformServices {
    @Override
    public void pokeForRateNotification() {

    }

    @Override
    public boolean pokeForNetworkStatus() {
        return false;
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void testCrash() {

    }
}
