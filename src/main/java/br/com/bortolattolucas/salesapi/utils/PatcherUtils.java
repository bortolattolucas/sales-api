package br.com.bortolattolucas.salesapi.utils;

import java.lang.reflect.Field;

public class PatcherUtils {

    public static <T> void patch(T origem, T destino) throws IllegalAccessException {
        Class<?> objClass = origem.getClass();
        Field[] objFields = objClass.getDeclaredFields();

        for (Field field : objFields) {
            field.setAccessible(true);

            Object value = field.get(destino);
            if (value != null) {
                field.set(origem, value);
            }

            field.setAccessible(false);
        }
    }
}
