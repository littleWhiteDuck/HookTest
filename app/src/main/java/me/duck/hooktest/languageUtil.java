package me.duck.hooktest;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

public class languageUtil {


    public static Context attachBaseContext(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLanguageLocale();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            LocaleList localeList = new LocaleList(locale);
            configuration.setLocales(localeList);
            return context.createConfigurationContext(configuration);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(new LocaleList(locale));
            } else {
                configuration.locale = locale;
                DisplayMetrics dm = resources.getDisplayMetrics();
                resources.updateConfiguration(configuration, dm);
            }
            return context;
        }
    }

    private static Locale getLanguageLocale() {
        Locale curLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            curLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            curLocale = Locale.getDefault();
        }
        curLocale.getLanguage();
        if (curLocale.getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage()) || curLocale.getLanguage().equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
            return Locale.CHINESE;
        } else {
            return Locale.ENGLISH;
        }
    }
}
