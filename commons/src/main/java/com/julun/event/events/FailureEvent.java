package com.julun.event.events;

import com.julun.event.EventConstants;

/**
 * Created by danjp on 2015/12/10.
 * 请求失败EventBus处理类 OnFailure
 */
public class FailureEvent extends BaseSimpleEvent {
    public FailureEvent() {
        super(EventConstants.EventCodes.SHOW_RESULT.getCode());
    }

    public FailureEvent(String data) {
        this();
        this.success = false;
        this.extraMessage = data;
    }
}
