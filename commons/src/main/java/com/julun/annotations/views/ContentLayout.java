package com.julun.annotations.views;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 以注解的方式注入需要的布局文件.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ContentLayout {
    /**
     * 需要用的layout
     * @return
     */
    public int value();
}
