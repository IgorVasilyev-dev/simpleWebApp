package com.mastery.simplewebapp.util;

import com.mastery.simplewebapp.error.exception.SQLConstraintException;
import com.mastery.simplewebapp.util.annotahion.NotNullOrEmptyData;

import java.lang.reflect.Field;

public class DetectNullOrEmptyFields {

    public static <T> void checkFields(T t) {

        if (t != null) {
            Field[] srcFields = t.getClass().getDeclaredFields();
            for (Field srcField : srcFields) {
                if (srcField.isAnnotationPresent(NotNullOrEmptyData.class)) {
                    srcField.setAccessible(true);
                    try {
                        Object obj = srcField.get(t);
                        if (obj == null) {
                            throw new SQLConstraintException("ОШИБКА: значение NULL в поле " + srcField.getName() +
                                    " отношения " + t.getClass().getName() + " нарушает ограничение NOT NULL");
                        } else {
                            if( srcField.getType() == String.class) {
                                String value = (String) obj;
                                if (value.trim().isEmpty()) {
                                    throw new SQLConstraintException("ОШИБКА: значение Empty в поле " + srcField.getName() +
                                            " отношения " + t.getClass().getName() + " нарушает ограничение NOT Empty");
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

