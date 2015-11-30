package com.julun.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.julun.commons.reflect.ReflectUtil;
import com.julun.commons.reflect.base.ParameterizedTypeSimpleImpl;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 需要的时候再添加.
 */
public class JsonHelper {
    public static final Gson gsonDayTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static final Gson gsonDay = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public static  <T> T fromJson(String json, Type type){
        return gsonDayTime.fromJson(json,type);
    }

    public static <T> T fromJson(String json, Class<T> type){
        return gsonDayTime.fromJson(json,type);
    }

    public static <T> List<T> toList(String json,Class<T> type){
        return gsonDayTime.fromJson(json, ReflectUtil.type(List.class, type));
    }

    public static <T> T[] toArray (String json,Class<T> type){
        return gsonDayTime.fromJson(json, ReflectUtil.array(type) );
    }

    public static <T> T fromJson(Reader reader, Type type){
        return gsonDayTime.fromJson(reader, type);
    }

    public static String toJson(Object object){
        return gsonDayTime.toJson(object);
    }

}
