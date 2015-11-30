package com.julun.event.processor;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2015-11-23.
 */
public interface EventSubscriber {
    /**
     * @param bus
     */
    public void setMainEventBus(EventBus bus);

    public EventBus getMainEventBus();
}
