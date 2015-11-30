package com.julun.event.events;

import com.julun.event.EventConstants;

public class DataChangeEvent<T> extends BaseSimpleEvent {
    /**
     * 变更的数据.
     */
    private T data;

    public DataChangeEvent(){
        super(EventConstants.EventCodes.SHOW_RESULT.getCode());
    }

    public DataChangeEvent(T data) {
        this();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
