package com.julun.container;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.BusiBaseService;
import com.julun.commons.reflect.ReflectUtil;
import com.julun.commons.reflect.base.ClassInfo;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.event.EventBusCacher;
import com.julun.event.EventBusUtils;
import com.julun.exceptions.ConfigException;
import com.julun.utils.CollectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 把Activity和Fragment当作容器来处理,这里做容器的初始化工作.并提供一些简单的语法糖.
 */
public class BaseContainerInitializer {

    public static <T extends View> T findView(View root, int id) {
        return (T) root.findViewById(id);
    }


    public static <T extends View, E extends Activity> T findView(E container, int id) {
        return (T) container.findViewById(id);
    }

    /**
     * 初始化activity.
     *
     * @param activity
     * @param savedInstanceState
     */
    public static void initActivity(BaseActivity activity, Bundle savedInstanceState) {
        ClassInfo store = ReflectUtil.getClassStore(activity);
        ContentLayout annotation = store.getAnnotation(ContentLayout.class);
        if (null == annotation) {
            throw new RuntimeException(new ConfigException("没有配置layout的注解") );
        }
        activity.setContentView(annotation.value());
        ButterKnife.bind(activity);
        Log.d(activity.getClass().getName(), "onCreate() called with: " + "");


        //注册EventBus  这两个方法有先后顺序...先给容器注册,然后后面初始化 服务类的时候,需要用到容器里的EventBus
        registerEventBus(activity);
        //初始化业务服务类对象
        initBusiServiceObjects(activity, store);

        //调用初始化之后的方法...
        callOnInitViews(activity, store);
    }

    /**
     * 视图初始化完毕之后.
     *
     * @param invoker
     * @param store
     */
    private static void callOnInitViews(UIContainerEvnProvider invoker, ClassInfo store) {
        //自定义的初始化方法或者其他
        List<Method> methods = store.getMethodWithAnnotation(AfterInitView.class);
        if (CollectionHelper.isNotEmpty(methods)) {
            if(methods.size()>1){
                throw new RuntimeException(new ConfigException("暂不支持配置多个 @AfterInitView 方法,请查看类内部是否有多个方法加了 @AfterInitView 注解 ") );
            }

            for (Method method : methods) {
                method.setAccessible(true);
                try {
                    method.invoke(invoker);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private static void registerEventBus(UIContainerEvnProvider invoker) {
        EventBus bus = EventBusUtils.getNonDefaultEventBus();
        EventBusCacher.getEventBus(invoker,EventBusCacher.BUS_TYPE_MAIN);
        invoker.setMainEventBus(bus);
        bus.register(invoker);
    }

    private static void initBusiServiceObjects(UIContainerEvnProvider invoker, ClassInfo store) {
        List<Field> fields = store.getFieldsWithAnnotation(BusinessBean.class);
        Class rawClass = store.getRawClass();
        for(Field field:fields){
            field.setAccessible(true);
            BusinessBean anno = field.getAnnotation(BusinessBean.class);
            Class<? extends BusiBaseService> targetClass = anno.serviceClass();
            if(targetClass == null || targetClass.equals(BusiBaseService.class)){
                targetClass = (Class<? extends BusiBaseService>) field.getType();
            }
            ClassInfo busiClass = ReflectUtil.getClassStore(targetClass);
            boolean needContext = anno.needContext();
            Constructor constructor = busiClass.getConstructorByArgType(needContext ? Context.class : null);
            if(null == constructor){
                throw new RuntimeException(new ConfigException("服务类不符合要求,未能找到合适的构造函数."));
            }
            constructor.setAccessible(true);
            try {
                BusiBaseService val = (BusiBaseService) (needContext ? constructor.newInstance(invoker.getContextActivity()):constructor.newInstance());
                val.setMainEventBus4Post(invoker.getMainEventBus());
                field.set(invoker,val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 初始化Fragment.
     *
     * @param fragment
     * @param container
     * @param savedInstanceState @return
     */
    public static View initFragment(BaseFragment fragment, ViewGroup container, Bundle savedInstanceState) {
        ClassInfo store = ReflectUtil.getClassStore(fragment);
        ContentLayout annotation = store.getAnnotation(ContentLayout.class);
        if (null == annotation) {
            throw new IllegalArgumentException("没有配置layout的注解");
        }
        View view = fragment.getLayoutInflater().inflate(annotation.value(), container, false);
        ButterKnife.bind(fragment, view);

        //注册EventBus  这两个方法有先后顺序...先给容器注册,然后后面初始化 服务类的时候,需要用到容器里的EventBus
        registerEventBus(fragment);
        //初始化业务服务类对象
        initBusiServiceObjects(fragment, store);

        callOnInitViews(fragment, store);
        return view;
    }

}
