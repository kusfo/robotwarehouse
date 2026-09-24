package com.teenagemutantninjacoders.robotwarehouse.android.providers;

import android.content.Context;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;

public class AndroidIdProvider implements IdProvider {
    private final Context context;

    public AndroidIdProvider(Context context) {
        this.context = context;
    }

    @Override
    public String getAchievementId(String achievement) {
        int identifier = context.getResources().getIdentifier(achievement, "string", context.getPackageName());
        return context.getResources().getString(identifier);
    }

    @Override
    public String getTrackingId(String event) {
        int identifier = context.getResources().getIdentifier(event, "string", context.getPackageName());
        return context.getResources().getString(identifier);
    }
}
