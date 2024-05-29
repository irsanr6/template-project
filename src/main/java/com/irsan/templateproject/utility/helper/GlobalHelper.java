package com.irsan.templateproject.utility.helper;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 29/05/2024
 */
public class GlobalHelper {

    public static <T> String toString(T object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object);
    }

}
