package com.julun.event.events;

import com.julun.event.EventConstants;

/**
 * 为方便处理EventBus事件的处理,所有的自定义事件必须继承这个类.方便以泛型的方式取值.
 */
public class BaseSimpleEvent {
    /**
     * 消息代码,接受者自行决定.也可以参考 {@link EventConstants.EventCodes}
     */
    protected int code;

    /**
     * 简单的表述发送这个消息的业务是否成功了.
     */
    protected boolean success = true;
    /**
     * 额外的消息,一般是请求失败的错误信息.
     */
    protected String extraMessage;

    /**
     * @param whatCode
     *            可以自定义.也可以参考 {@link EventConstants.EventCodes}
     * @see EventConstants.EventCodes
     */
    public BaseSimpleEvent(int whatCode) {
        this.code = whatCode;
    }

    public BaseSimpleEvent(int code, boolean success) {
        super();
        this.code = code;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }
}
