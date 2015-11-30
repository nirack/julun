package com.julun.annotations.business;

import com.julun.business.BusiBaseService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务实现类的注入注解.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessBean {

    /**
     * 需要被实例化的类.
     * @return
     */
    public Class<? extends BusiBaseService> serviceClass() default BusiBaseService.class;

    /**
     * 实例化的时候,是否需要Context.
     * @return
     */
    public boolean needContext() default false;
}
