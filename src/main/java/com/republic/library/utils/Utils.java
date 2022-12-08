package com.republic.library.utils;

import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Utils {
    public static String checkAndReturnCategories(String categories) {
        if (categories.isEmpty()) return null;

        var categoriesList = Arrays
                .stream(categories.trim().split(", "))
                .distinct().collect(Collectors.toList());

        StringBuilder categoriesString = new StringBuilder();

        for (var category : categoriesList) {
            categoriesString.append(category).append(", ");
        }
        categoriesString.delete(categoriesString.length() - 2, categoriesString.length());

        return categoriesString.toString();
    }

    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
