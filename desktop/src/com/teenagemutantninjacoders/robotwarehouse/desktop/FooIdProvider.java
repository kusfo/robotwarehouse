package com.teenagemutantninjacoders.robotwarehouse.desktop;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;

public class FooIdProvider implements IdProvider {
    @Override
    public String getAchievementId(String achievement) {
        return "";
    }

    @Override
    public String getTrackingId(String event) {
        return "";
    }
}
