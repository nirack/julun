package com.julun.container;

import android.content.Context;
import android.os.Handler;

import com.julun.event.processor.EventSubscriber;

/**
 *
 */
public interface UIContainerEvnProvider extends EventSubscriber{
    /**
     * 初始化的时候需要context对象.
     * 需要获取到Activity作为Context,
     * 如果是Fragments,返回getContext,如果是Activity,返回this
     * @return
     */
    public Context getContextActivity();

    public Handler getHandler();

}
