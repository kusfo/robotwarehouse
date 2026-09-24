package com.teenagemutantninjacoders.robotwarehouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.teenagemutantninjacoders.robotwarehouse.ads.iOSAdHandler;


import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.notifications.iOSNotificationHandler;
import com.teenagemutantninjacoders.robotwarehouse.providers.iOSIdProvider;
import com.teenagemutantninjacoders.robotwarehouse.tracking.iOsFirebaseEventTracker;

import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.pods.firebase.core.FIRApp;
import org.robovm.pods.firebase.crashlytics.FIRCrashlytics;
import org.robovm.pods.google.mobileads.GADFullScreenContentDelegate;
import org.robovm.pods.google.mobileads.GADFullScreenPresentingAd;
import org.robovm.pods.google.mobileads.GADInitializationStatus;
import org.robovm.pods.google.mobileads.GADMobileAds;
import org.robovm.pods.google.mobileads.GADRequest;
import org.robovm.pods.reachability.NetworkReachability;
import org.robovm.pods.reachability.NetworkStatus;
import org.robovm.rt.Signals;

import de.golfgl.gdxgamesvcs.GameCenterClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;


public class IOSLauncher extends IOSApplication.Delegate implements PlatformServices {

    private iOSNotificationHandler notificationHandler;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.allowIpod = true;
        NoGameServiceClient noGameServiceClient = new NoGameServiceClient();
        iOsFirebaseEventTracker iOsFirebaseEventTracker = new iOsFirebaseEventTracker();
        iOSAdHandler adHandler = new iOSAdHandler();
        notificationHandler = new iOSNotificationHandler();
        iOSIdProvider iOSIdProvider = new iOSIdProvider();

        Signals.installSignals(new Signals.InstallSignalsCallback() {
            @Override
            public void install() {
                FIRApp.configure();
            }
        });
        FIRCrashlytics.registerDefaultJavaUncaughtExceptionHandler();
        NSUserDefaults standardUserDefaults = NSUserDefaults.getStandardUserDefaults();
        NSDictionary<NSString, ?> defaultValues = new NSDictionary<>();
        defaultValues.setAssociatedObject(iOSAdHandler.AD_MOB_LAPSE_CICLE, 5);
        defaultValues.setAssociatedObject(iOSNotificationHandler.RATE_APP_LAPSE_SEQUENCE, 10);
        defaultValues.setAssociatedObject(iOSNotificationHandler.RATE_APP_LAPSE_TIME, 125);
        standardUserDefaults.registerDefaults(defaultValues);
        final RobotWarehouseGame robotWarehouseGame = new RobotWarehouseGame(noGameServiceClient, iOsFirebaseEventTracker, adHandler, this, iOSIdProvider, "pop", false) {
            @Override
            public void create() {
                this.playServices = new GameCenterClient(((IOSApplication) Gdx.app).getUIViewController());
                super.create();
            }
        };
        adHandler.initialize(new GADFullScreenContentDelegate() {
            @Override
            public void adDidRecordImpression(GADFullScreenPresentingAd ad) {

            }

            @Override
            public void adDidRecordClick(GADFullScreenPresentingAd ad) {

            }

            @Override
            public void didFailToPresentFullScreenContent(GADFullScreenPresentingAd ad, NSError error) {
                Foundation.log("iOSLauncher failed to present ad", error);
            }

            @Override
            public void adDidPresentFullScreenContent(GADFullScreenPresentingAd ad) {
                Foundation.log("iOSLauncher ad presented");
            }

            @Override
            public void adWillDismissFullScreenContent(GADFullScreenPresentingAd ad) {
                Foundation.log("iOSLauncher ad dismissed");
                robotWarehouseGame.adClosed();
            }

            @Override
            public void adDidDismissFullScreenContent(GADFullScreenPresentingAd ad) {

            }
        });
        return new IOSApplication(robotWarehouseGame, config);
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        GADMobileAds.sharedInstance().start(new VoidBlock1<GADInitializationStatus>() {
            @Override
            public void invoke(GADInitializationStatus status) {
                GADMobileAds.sharedInstance().getRequestConfiguration().setTestDeviceIdentifiers(new NSArray<>(GADRequest.GADSimulatorID()));
            }
        });
        return super.didFinishLaunching(application, launchOptions);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();

    }

    @Override
    public void pokeForRateNotification() {
        ((IOSApplication) Gdx.app).getVersion();
        notificationHandler.pokeRateNotification();

    }

    @Override
    public boolean pokeForNetworkStatus() {
        NetworkReachability internetConnection = NetworkReachability.forInternetConnection();
        NetworkStatus currentReachabilityStatus = internetConnection.getCurrentReachabilityStatus();
        if (currentReachabilityStatus == NetworkStatus.NotReachable) {
            return false;
        }
        return true;
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void testCrash() {

    }
}