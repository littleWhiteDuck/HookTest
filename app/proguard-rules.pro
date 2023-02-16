# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

 -keep class me.duck.hooktest.MainActivity {
    private void initView();
    private void alterParams(int, boolean, java.lang.String);
    private void breakPerform();
    private void changeStaticFields();
    private java.lang.String getReturnValue();
    private static int scores;
    private static boolean isGood;
    private me.duck.hooktest.bean.UseBean useBean;
 }
 -keep class me.duck.hooktest.bean.UseBean

 -repackageclasses "littleWhiteDuck"

