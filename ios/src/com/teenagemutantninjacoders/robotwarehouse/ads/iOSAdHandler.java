package com.teenagemutantninjacoders.robotwarehouse.ads;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.AdServices;

import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.block.VoidBlock2;
import org.robovm.pods.google.mobileads.GADFullScreenContentDelegate;
import org.robovm.pods.google.mobileads.GADInterstitialAd;
import org.robovm.pods.google.mobileads.GADRequest;

public class iOSAdHandler implements AdServices {
    public static final String AD_MOB_LAPSE_CICLE = "admoblapsecycle";
    private static final String AD_MOB_LAPSE_CICLE_VALUE = "admob_lapse_cycle";
    private GADInterstitialAd interstitialAd;
    private boolean mustShowAd;
    private boolean intestitialLoaded;
    private GADFullScreenContentDelegate fullScreenContentDelegate;

    public void initialize(GADFullScreenContentDelegate fullScreenContentDelegate) {
        this.fullScreenContentDelegate = fullScreenContentDelegate;
        mustShowAd = false;
        intestitialLoaded = false;
    }
    //check this:
    /*
        NSString *filePath = [[NSBundle mainBundle] pathForResource:@"strings" ofType:@"plist"];
        NSDictionary *dict = [[NSDictionary alloc] initWithContentsOfFile:filePath];
        NSString *string1 = [dict objectForKey:@"string1"];
    */

    @Override
    public void pokeInterstitial() {
        NSUserDefaults standardUserDefaults = NSUserDefaults.getStandardUserDefaults();

        long adMobCycle = standardUserDefaults.getLong(AD_MOB_LAPSE_CICLE);
        if(adMobCycle == 0) {
            standardUserDefaults.put(AD_MOB_LAPSE_CICLE, 5); //AD_MOB_LAPSE_CICLE_VALUE
            mustShowAd = true;
        } else {
            adMobCycle--;
            standardUserDefaults.put(AD_MOB_LAPSE_CICLE, adMobCycle);
        }
        standardUserDefaults.synchronize();
    }

    @Override
    public boolean mustShowInterstitial() {
        return mustShowAd;
    }

    @Override
    public void loadInterstitialAd() {
        final String interstitialID = "ca-app-pub-3940256099942544/4411468910";//TODO: Move to xml
        interstitialAd = new GADInterstitialAd();
        intestitialLoaded = false;
        GADInterstitialAd.load(interstitialID, new GADRequest(), new VoidBlock2<GADInterstitialAd, NSError>() {
            @Override
            public void invoke(GADInterstitialAd gadInterstitialAd, NSError nsError) {
                if(nsError == null) {
                    interstitialAd = gadInterstitialAd;
                    interstitialAd.setFullScreenContentDelegate(fullScreenContentDelegate);
                    intestitialLoaded = true;
                } else {
                    interstitialAd = null;
                    intestitialLoaded = false;
                    Foundation.log("iOSAdHandler: InterstialAd not laoded, error:", nsError);
                }
            }
        });
    }

    @Override
    public boolean isInterstitialLoaded() {
        return intestitialLoaded;
    }

    @Override
    public void showInterstitialAd() {
        if(intestitialLoaded && interstitialAd != null) {
            UIViewController rootViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();
            NSError.NSErrorPtr errorFromPresenting = null;
            interstitialAd.canPresentFromRootViewController(rootViewController,errorFromPresenting);
            if(errorFromPresenting.get() == null) {
                interstitialAd.presentFromRootViewController(rootViewController);
            } else {
                Foundation.log("iOSAdHandler: InterstialAd cannotbePresented, error:", errorFromPresenting.get());
            }
            mustShowAd = false;
        }

    }
}

