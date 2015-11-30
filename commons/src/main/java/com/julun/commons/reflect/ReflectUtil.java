package com.julun.commons.reflect;

import com.julun.commons.reflect.base.ClassInfo;
import com.julun.commons.reflect.base.ParameterizedTypeSimpleImpl;
import com.julun.datas.PageResult;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil {

    private ReflectUtil() {
    }

    private static final Map<String, ParameterizedType> typeMap = new HashMap<>();

    private static final Map<String, ClassInfo> classStoreMap = new HashMap<>();

    public static ParameterizedType type(final Class<?> raw, final Type... args) {
        String key = getTypeKey(raw, args);
        ParameterizedType type = typeMap.get(key);
        if (typeMap == null) {
            type = new ParameterizedTypeSimpleImpl(raw, args);
            typeMap.put(key, type);
        }
        return type;
    }

    private static String getTypeKey(Class<?> raw, Type... args) {
        String result = raw.getName();
        if (args != null) {
            for (Type type : args) {
                result += ":" + type.toString();
            }
        }
        return result;
    }

    /**
     * 生成List<Bean>形式的泛型信息
     *
     * @param clazz 泛型的具体类
     * @return List<clazz>形式的泛型Type
     */
    public static Type list(Type clazz) {
        return type(List.class, clazz);
    }

    /**
     * 获取分页 PageResult<Bean> 的泛型  信息.
     *
     * @param clazz
     * @return
     */
    public static Type pager(Type clazz) {
        return type(PageResult.class, clazz);
    }

    /**
     * 生成Bean[]形式的泛型信息
     */
    public static Type array(Class<?> clazz) {
        return type(Array.newInstance(clazz, 0).getClass());
    }

    /**
     * 生成Map<key,serviceClass>形式的泛型Type
     *
     * @param key   key的泛型
     * @param value value的泛型
     * @return Map<key,serviceClass>形式的泛型Type
     */
    public static Type map(Type key, Type value) {
        return type(Map.class, key, value);
    }

    /**
     * 生成Map<String,serviceClass>形式的泛型Type
     *
     * @param value value的泛型
     * @return Map<String,serviceClass>形式的泛型Type
     */
    public static Type mapStr(Type value) {
        return map(String.class, value);
    }

    public static ClassInfo getClassStore(Class<?> klass) {
        String key = klass.getName();
        ClassInfo classInfo = classStoreMap.get(key);
        if (null == classInfo) {
            classInfo = new ClassInfo(klass);
            classStoreMap.put(key, classInfo);
        }
        return classInfo;
    }

    public static <T> ClassInfo getClassStore(T bean) {
        return getClassStore(bean.getClass());
    }

    public static Class<?> getClass(String initializerClass) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(initializerClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass;
    }
}
