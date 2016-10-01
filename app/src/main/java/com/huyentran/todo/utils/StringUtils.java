package com.huyentran.todo.utils;

/**
 * String utils.
 */
public final class StringUtils {

    /**
     * Checks whether or not a string is invalid (i.e. null, empty, or otherwise entirely composed of whitespace characters)
     * @param str the string to check
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        String strNoWhitespace = str.replaceAll("\\s","");
        return strNoWhitespace.isEmpty();
    }
}
