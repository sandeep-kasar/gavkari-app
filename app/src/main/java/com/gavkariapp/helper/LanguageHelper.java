package com.gavkariapp.helper;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class LanguageHelper {
    public static final void setAppLocale(String language, Application activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = activity.getResources().getConfiguration();
            config.locale = locale;
            activity.getApplicationContext().getResources().updateConfiguration(config,
                    activity.getResources().getDisplayMetrics());

        } else {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = activity.getResources().getConfiguration();
            config.locale = locale;
            activity.getApplicationContext().getResources().updateConfiguration(config,
                    activity.getResources().getDisplayMetrics());
        }
    }
}