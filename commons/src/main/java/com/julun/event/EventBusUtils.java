package com.julun.event;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015-11-22.
 */
public class EventBusUtils {

    /**
     * 获取非默认的EventBus.
     * @return
     */
    public static EventBus getNonDefaultEventBus() {
        return EventBus.builder().build();
    }
}
