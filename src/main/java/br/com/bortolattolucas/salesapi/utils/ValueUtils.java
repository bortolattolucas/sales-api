package br.com.bortolattolucas.salesapi.utils;

public class ValueUtils {

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isNullOrEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }
}
