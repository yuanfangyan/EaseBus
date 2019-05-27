package com.yuan.buslibrary;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EaseBus {
    private static volatile EaseBus instance;
    private Map<Object, List<SubscribleMethod>> cacheMap;
    private Handler mHandler ;

    public static EaseBus getDefault() {
        if (instance == null) {
            synchronized (EaseBus.class) {
                if (instance == null) {
                    instance = new EaseBus();
                }
            }
        }
        return instance;
    }

    private EaseBus() {
        cacheMap = new HashMap<>();
        mHandler= new Handler();
    }

    public void register(Object object) {
        List<SubscribleMethod> list = cacheMap.get(object);
        if (list == null) {
            list = findSubscribleMethods(object);
            cacheMap.put(object, list);
        }
    }

    private List<SubscribleMethod> findSubscribleMethods(Object object) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = object.getClass();
        //获取当前类的方法数组
        Method[] methods = clazz.getDeclaredMethods();

        while (clazz != null) {

            //找父类的时候判读是否是系统级别的父类
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }

            for (Method method : methods) {
                //找到 Subscrible注解的方法
                Subscrible annotation = method.getAnnotation(Subscrible.class);
                if (annotation == null) {
                    continue;
                }
                //判断带有Subscrible注解方发中的参数类型
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    throw new IllegalStateException("参数必须只为一个");
                }
                ThreadMode threadMode = annotation.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode, types[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void unRegister(Object object) {
        cacheMap.remove(object);
    }

    public void post(final Object type) {
        //直接循环map里的方法，找到对应的然后回调
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for (final SubscribleMethod subscribleMethod : list) {
                if (subscribleMethod.getType().isAssignableFrom(type.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribleMethod, obj, type);
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, type);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, type);
                                    }
                                });
                            } else {
                                invoke(subscribleMethod, obj, type);
                            }
                            break;
                    }


                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object type) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
