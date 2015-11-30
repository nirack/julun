package com.julun.event.events;

import com.julun.event.EventConstants;

/**
 * 请求数据的事件,无论从哪里请求，都要异步执行.
 */
public class SendRequestEvent<T> extends BaseSimpleEvent {
    protected int requestCode;
    protected T requestParams;
    public SendRequestEvent() {
        super(EventConstants.EventCodes.SHOW_RESULT.getCode());
    }
}
