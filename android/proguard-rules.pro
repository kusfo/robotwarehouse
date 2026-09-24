-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
   <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class com.teenagemutantninjacoders.robotwarehouse.data.dtos.** {
   *;
}

-keepclassmembers class com.teenagemutantninjacoders.robotwarehouse.display.AssetFileAttributes {
    *;
}

-keepclassmembers class com.teenagemutantninjacoders.robotwarehouse.display.AtlasSprite {
    *;
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keep class com.badlogic.gdx.**{
    **[] $VALUES;
     *;
 }

-keep class com.google.**{
    **[] $VALUES;
     *;
 }
 -dontwarn com.google.**

 -keep class com.crashlytics.** { *; }
 -dontwarn com.crashlytics.**

