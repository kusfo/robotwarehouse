package com.teenagemutantninjacoders.robotwarehouse.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.teenagemutantninjacoders.robotwarehouse.BuildConfig;
import com.teenagemutantninjacoders.robotwarehouse.R;
import com.teenagemutantninjacoders.robotwarehouse.android.ads.AndroidAdHandler;
import com.teenagemutantninjacoders.robotwarehouse.android.notifications.AndroidNotificationHandler;
import com.teenagemutantninjacoders.robotwarehouse.android.providers.AndroidIdProvider;
import com.teenagemutantninjacoders.robotwarehouse.android.tracking.AndroidFirebaseEventTracker;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;

import de.golfgl.gdxgamesvcs.GpgsClient;


public class AndroidLauncher extends AndroidApplication implements PlatformServices {
    private RobotWarehouseGame robotWarehouseGame;
    private AndroidNotificationHandler androidNotificationHandler;
    private AndroidAdHandler androidAdHandler;
    private AndroidFirebaseEventTracker androidFirebaseEventTracker;
    private GpgsClient gpsClient;
    private IdProvider androidIdProvider;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidNotificationHandler = new AndroidNotificationHandler(this);
        androidAdHandler = new AndroidAdHandler(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        androidFirebaseEventTracker = new AndroidFirebaseEventTracker(FirebaseAnalytics.getInstance(this));
        androidIdProvider = new AndroidIdProvider(getContext());
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        gpsClient = new GpgsClient().initialize(this, true);
        String deviceID = Build.MODEL + " " + Build.VERSION.RELEASE;
        if(BuildConfig.DEBUG) {
            robotWarehouseGame = new RobotWarehouseGame(gpsClient, androidFirebaseEventTracker, androidAdHandler, this, androidIdProvider, deviceID,true);
        } else {
            robotWarehouseGame = new RobotWarehouseGame(gpsClient, androidFirebaseEventTracker, androidAdHandler, this, androidIdProvider, deviceID,false);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(robotWarehouseGame, config);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.i("AndroidLauncher", "MobileAds initialized");
            }
        });
        //initialize FireBaseRemoteConfig
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            Log.i("AndroidLauncher","Remote Config fetched ok");
                        } else {
                            Log.i("AndroidLauncher","Remote Config error fecthing");
                        }
                    }
                });
        androidAdHandler.initialize(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.w("AndroidLauncher", "Ad dismissed");
                robotWarehouseGame.adClosed();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.w("AndroidLauncher", adError.getMessage());
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.w("AndroidLauncher", "Ad Shown");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        androidNotificationHandler.manageStopNotifications();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(gpsClient != null) {
            gpsClient.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void pokeForRateNotification() {
        androidNotificationHandler.pokeRateNotification();
    }

    @Override
    public boolean pokeForNetworkStatus(){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    @Override
    public void showLoginError() {
        Toast.makeText(this, "Cannot Login",Toast.LENGTH_LONG).show();
    }

    @Override
    public void testCrash() {
        throw new RuntimeException("Test Crash");
    }
}
