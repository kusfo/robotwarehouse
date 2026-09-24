#!/bin/sh
mkdir "${ANDROID_HOME}/licenses" || true
echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" >> "${ANDROID_HOME}/licenses/android-sdk-license"
echo "84831b9409646a918e30573bab4c9c91346d8abd" > "${ANDROID_HOME}/licenses/android-sdk-preview-license"
echo "y" | android update sdk --no-ui -a --filter "android-32"
echo "y" | android update sdk --no-ui -a --filter "build-tools-33.0.2"
cd robotwarehouse
./gradlew android:prepareAlphaVersion
./gradlew android:assembleRelease android:publishReleaseApk
./gradlew android:promoteArtifact --from-track internal --promote-track alpha