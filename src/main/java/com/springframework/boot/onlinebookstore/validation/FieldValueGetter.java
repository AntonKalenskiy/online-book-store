package com.springframework.boot.onlinebookstore.validation;

import org.apache.commons.beanutils.PropertyUtils;

public class FieldValueGetter {
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        return PropertyUtils.getProperty(obj, fieldName);
    }
}
