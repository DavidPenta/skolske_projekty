package com.musicverse.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Localozator {
    private static Locale locale = Locale.getDefault();

    public static void setLocale(Locale l){
        locale = l;
    }

    public static ResourceBundle getResourceBundle(){
        return ResourceBundle.getBundle("bundle.Language", locale);
    }

}
