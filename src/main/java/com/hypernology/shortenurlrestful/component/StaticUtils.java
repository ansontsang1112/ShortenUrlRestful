package com.hypernology.shortenurlrestful.component;

public class StaticUtils {
    public static String alphabetic = "0123456789abcdefghijklmnpqrstuvwxyz";

    public static <T> boolean notNullableCheck(T input) {
        if(input != null || input.toString() != "") return true;
        return false;
    }

    public static boolean urlValidation(String url) {
        if(url.contains("http://") || url.contains("https://")) return true;
        return false;
    }
}
