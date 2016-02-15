# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keep class com.parse.*{ *; }
-keep class com.jakewharton.*{ *; }
-keep class android.support.v7.widget.SearchView { *; }
-keep @com.facebook.common.internal.DoNotStrip class *
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn butterknife.internal.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.jakewharton.**
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclassmembers class * {
@com.facebook.common.internal.DoNotStrip *;
}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
