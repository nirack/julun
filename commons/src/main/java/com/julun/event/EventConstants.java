package com.julun.event;

import com.julun.event.events.BaseSimpleEvent;

/**
 * EventBus用到的一些常量.
 */
public class EventConstants {

    public static int defaultEventCode(){
        return EventCodes.NON_SENSE.getCode();
    }

    /**
     * 为 event的 what字段提供一些定义好的枚举.
     *
     * @see BaseSimpleEvent
     */
    public enum EventCodes {
        /**
         * 没啥好填的时候就这个吧.
         */
        NON_SENSE(-1),
        /**
         * 告知结果.
         */
        SHOW_RESULT(0),
        /**
         * 发送请求.
         */
        SEND_REMOTE_REQUEST(1)
        ;
        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        private EventCodes(int code) {
            this.code = code;
        }

    }
}
