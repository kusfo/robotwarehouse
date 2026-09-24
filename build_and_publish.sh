#!/bin/sh
echo "yes" | sdkmanager "platform-tools"
echo "yes" | sdkmanager "platforms;android-32"
echo "yes" | sdkmanager "build-tools;33.0.2"
echo "yes" | sdkmanager "extras;android;m2repository"
echo "yes" | sdkmanager "extras;google;m2repository"
echo "yes" | sdkmanager "extras;google;instantapps"
echo "yes" | sdkmanager --licenses
cd robotwarehouse
./gradlew android:prepareInternalVersion
./gradlew android:assembleRelease android:publishReleaseApk
