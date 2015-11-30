package com.julun.utils;

import com.julun.datas.beans.County;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 一些集合类的小语法糖.
 */
public class CollectionHelper {
    /**
     * 判断集合是否为空, null 或者空集合都算为空.
     * @param collection 要判断的集合.
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @see #isEmpty(Collection)
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * 保证传入的结合不为空.
     * @param list 要操作的集合.
     * @param <T>
     */
    public static <T> void dontBeEmpty(List<T> list){
        if(isEmpty(list)){
            list = new ArrayList<T>();
        }
    }

    public  static <T> List<T> reverse(List<T> data) {
        List<T> newOne = new ArrayList<>();
        for (int index = data.size()-1; index >=0; index--) {
            newOne.add(data.get(index));
        }
        return newOne;
    }
}
