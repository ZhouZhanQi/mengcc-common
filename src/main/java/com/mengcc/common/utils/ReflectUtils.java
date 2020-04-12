package com.mengcc.common.utils;

import com.mengcc.common.exceptions.FrameworkException;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author zhouzq
 * @version 1.0
 * @date 2020/3/25 5:45 下午
 * @desc 反射工具类
 */
public class ReflectUtils {

    private ReflectUtils() {
    }

    /**
     * 利用反射获取指定对象的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Optional<Object> getFieldValue(Object obj, String fieldName) throws IllegalAccessException {
        Object result = null;
        Optional<Field> field = getField(obj, fieldName);
        if (field.isPresent()) {
            field.get().setAccessible(true);
            result = field.get().get(obj);
        }
        return Optional.ofNullable(result);
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Optional<Field> getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return Optional.ofNullable(field);
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
        Optional<Field> field = getField(obj, fieldName);
        if (field.isPresent()) {
            try {
                field.get().setAccessible(true);
                field.get().set(obj, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new FrameworkException("reflect set field value error");
            }
        }
    }

}
