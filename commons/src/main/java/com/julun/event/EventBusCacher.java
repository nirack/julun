package com.julun.event;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import de.greenrobot.event.EventBus;

/**
 * 缓存EventBus对象.
 * 管理四个缓存池.
 */
public class EventBusCacher {
    public static final int BUS_TYPE_MAIN = 1;
    public static final int BUS_TYPE_ASYN = 2;
    public static final int BUS_TYPE_POST_THREAD = 3;
    public static final int BUS_TYPE_BG = 4;

    private static final List<EventBus> list = new ArrayList<>();
    /**
     * ui线程
     */
    private static final WeakHashMap<Object, EventBus> UI_THREAD_CACHE = new WeakHashMap<>();
    /**
     * 异步
     */
    private static final WeakHashMap<Object, EventBus> ASYNC_CACHE = new WeakHashMap<>();
    /**
     * 当前线程
     */
    private static final WeakHashMap<Object, EventBus> POST_THREAD_CACHE = new WeakHashMap<>();
    /**
     * 后台线程.
     */
    private static final WeakHashMap<Object, EventBus> BACKGROUND_CACHE = new WeakHashMap<>();

    /**
     * 获取一个指定类型的eventbus.
     *
     * @param scripter
     * @param mode
     * @return
     */
    public static EventBus getEventBus(@NonNull Object scripter, int mode) {
        Map<Object, EventBus> cache = null;
        switch (mode) {
            case BUS_TYPE_ASYN:
                cache = ASYNC_CACHE;
                break;
            case BUS_TYPE_BG:
                cache = BACKGROUND_CACHE;
                break;
            case BUS_TYPE_MAIN:
                cache = UI_THREAD_CACHE;
                break;
            case BUS_TYPE_POST_THREAD:
                cache = POST_THREAD_CACHE;
                break;
        }
        if(cache == null){
            throw new RuntimeException("指定的类型不存在,请参考  " + EventBusCacher.class.getSimpleName() + " 类的几个常量数字");
        }
        EventBus eventBus = cache.get(scripter);
        if (null == eventBus) {
            eventBus = EventBusUtils.getNonDefaultEventBus();
            cache.put(scripter, eventBus);
        }
        return eventBus;
    }

    public static EventBus getUiThreadBus(Object scripter) {
        return getEventBus(scripter, BUS_TYPE_MAIN);
    }

}
