package com.julun.event.processor;

import com.julun.business.BusiBaseService;
import com.julun.container.UIContainerEvnProvider;
import com.julun.event.EventBusUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import de.greenrobot.event.EventBus;

/**
 * 注册分发事件的传播.
 *
 */
public class EventRegisterCenter {
    private static final WeakHashMap<UIContainerEvnProvider,EventBus> mainBusMap = new WeakHashMap<>();


    public static void register(UIContainerEvnProvider mainThread,BusiBaseService service){
        EventBus bus = EventBusUtils.getNonDefaultEventBus();
        bus.register(mainThread);

    }

    public static void unregister(UIContainerEvnProvider obj){
        EventBus eventBus = mainBusMap.get(obj);
        eventBus.unregister(obj);
    }

}
