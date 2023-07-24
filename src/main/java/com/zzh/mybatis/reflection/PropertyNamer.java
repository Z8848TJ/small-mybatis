package com.zzh.mybatis.reflection;

import java.util.Locale;

/**
 * @author: zzh
 * @description: 属性命名器
 */
public class PropertyNamer {

    private PropertyNamer() {}

    /**
     * 方法名转换为属性名
     */
    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        /*
         * 如果只有1个字母，转换为小写
         * 如果大于1个字母，第二个字母非大写，转换为小写
         */
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
    
}
