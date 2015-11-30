package com.julun.commons.reflect.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储类信息的容器.
 * 需要更多功能的时候再添加.
 */
public class ClassInfo {
    /**
     * 类
     */
    private Class rawClass;

    /**
     * 当前类里声明的field.
     */
    private List<Field> declaredFields;

    /**
     * 当前类里声明的 方法.
     */
    private List<Method> declaredMethods;

    private List<Constructor> declaredConstructors;

    private ClassInfo(){
    }

    public ClassInfo(@NonNull Class klass){
        this.rawClass = klass;
        Field[] fields = klass.getDeclaredFields();
        declaredFields = new ArrayList<>();
        for(Field field:fields){
            declaredFields.add(field);
        }
        declaredMethods = new ArrayList<>();
        Method[] methods = klass.getDeclaredMethods();
        for(Method method:methods){
            declaredMethods.add(method);
        }
        Constructor[] cons = klass.getDeclaredConstructors();
        this.declaredConstructors = new ArrayList<>();
        for(Constructor con:cons){
            declaredConstructors.add(con);
        }
    }

    public List<Method> getDeclaredMethods() {
        return declaredMethods;
    }

    public void setDeclaredMethods(List<Method> declaredMethods) {
        this.declaredMethods = declaredMethods;
    }

    public Class getRawClass() {
        return rawClass;
    }

    public void setRawClass(Class rawClass) {
        this.rawClass = rawClass;
    }

    public List<Field> getDeclaredFields() {
        return declaredFields;
    }

    public void setDeclaredFields(List<Field> declaredFields) {
        this.declaredFields = declaredFields;
    }

    /**
     * 获取类的注解.
     * @param anno
     * @param <T>
     * @return
     */
    public <T extends Annotation> T getAnnotation(@NonNull Class<? extends Annotation> anno) {
        Annotation annotation = rawClass.getAnnotation(anno);
        if(null == annotation){
            return null;
        }
        return (T) annotation;
    }

    /**
     * 获取带有某个注解的方法.
     * @param anno
     * @return
     */
    public List<Method> getMethodWithAnnotation(@NonNull Class<? extends Annotation> anno) {
        List<Method> result = new ArrayList<>();
        for (Method method:this.declaredMethods){
            if(method.isAnnotationPresent(anno)){
                result.add(method);
            }
        }
        return result;
    }

    /**
     * 查找所有带 指定的注解的方法.
     * @param anno
     * @return
     */
    public List<Field> getFieldsWithAnnotation(@NonNull Class<? extends Annotation> anno) {
        List<Field> fields = new ArrayList<>();
        for (Field field:this.declaredFields){
            if(field.isAnnotationPresent(anno)){
                fields.add(field);
            }
        }
        return fields;
    }

    public List<Method> getMethodByName(String name) {
        List<Method> list = new ArrayList<>();
        for (Method method:this.declaredMethods){
            if(method.getName().equals(name)){
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 获取带有某个指定类型参数的 方法.
     * @param name
     * @param contextClass
     * @return
     */
    public List<Method> getMethodByNameWithArgument(String name, Class<?> contextClass) {
        List<Method> list = new ArrayList<>();
        for (Method method:this.declaredMethods){
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(!method.getName().equals(name) || null == parameterTypes || parameterTypes.length == 0){
                    continue;
            }
            for (Class klass : parameterTypes){
                if(contextClass.equals(klass)){
                    list.add(method);
                    break;
                }
            }
        }
        return list;
    }

    /**
     * 获取构造函数.
     * 只返回一个.
     * 如果以后有更多需求,在做修改.
     * @param argType
     * @return
     */
    public Constructor getConstructorByArgType(Class<?> argType) {
        boolean needArg = null != argType;
        for(Constructor con:this.declaredConstructors){
            Class[] parameterTypes = con.getParameterTypes();
            if(!needArg && (parameterTypes == null || parameterTypes.length == 0)){
                return con;
            }else if(needArg && parameterTypes !=null && parameterTypes.length > 0){
                for(Class kl:parameterTypes){
                    if(kl.equals(argType)){
                        return con;
                    }
                }
            }
        }
        return null;
    }
}
