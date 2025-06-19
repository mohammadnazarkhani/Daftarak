package com.example.daftarak.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleHelper {

    public static Context setLocale(Context context, String languageCode) {
        Locale locale;
        switch (languageCode.toLowerCase()) {
            case "persian":
                locale = new Locale("fa");
                break;
            case "english":
                locale = Locale.ENGLISH;
                break;
            default:
                locale = Locale.getDefault();
                break;
        }

        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }

        return context;
    }
}
