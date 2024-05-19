package com.davidbelesp.mybookshelf.locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleHelper {

    public static Locale getCurrentLocale(Context context) {
        LocaleList localeList = context.getResources().getConfiguration().getLocales();
        if (localeList.isEmpty()) {
            return Locale.getDefault();
        }
        return localeList.get(0);
    }

    public static void setLocale(Context context, Locale l) {
        String languageCode = l.getLanguage();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        config.setLocale(locale);
        context.createConfigurationContext(config);
    }

}
