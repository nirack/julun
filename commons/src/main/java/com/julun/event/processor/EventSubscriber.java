package com.julun.event.processor;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 事件订阅者.
 */
public interface EventSubscriber {
    /**
     * @param bus
     */
    public void setMainEventBus(EventBus bus);

    public EventBus getMainEventBus();
}
