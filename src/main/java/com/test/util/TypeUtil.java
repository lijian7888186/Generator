package com.test.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijian
 * @date 2019/7/29 17:05
 * @desc
 */
public class TypeUtil {
    public static Map<String, Object> findClass(String classStr) {
        Map<String, Object> map = new HashMap<>();
        switch (classStr) {
            case "INT" :
                map.put("classType", Integer.class);
                map.put("className", "Integer");
                break;
            case "INTEGER" :
                map.put("classType", Integer.class);
                map.put("className", "Integer");
                break;
            case "VARCHAR" :
                map.put("classType", String.class);
                map.put("className", "String");
                break;
            case "TINYINT" :
                map.put("classType", Byte.class);
                map.put("className", "Byte");
                break;
            case "TIMESTAMP" :
                map.put("classType", Date.class);
                map.put("className", "Date");
                map.put("importClass", Date.class.getName());
                break;
            case "DATETIME" :
                map.put("classType", Date.class);
                map.put("className", "Date");
                map.put("importClass", Date.class.getName());
                break;
            case "BIGINT" :
                map.put("classType", Long.class);
                map.put("className", "Long");
                break;
            case "DECIMAL" :
                map.put("classType", BigDecimal.class);
                map.put("className", "BigDecimal");
                map.put("importClass", BigDecimal.class.getName());
                break;
            default:
                map.put("classType", Object.class);
                map.put("className", "Object");
                break;
        }
        return map;
    }
}
