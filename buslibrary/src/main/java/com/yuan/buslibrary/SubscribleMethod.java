package com.yuan.buslibrary;

import com.yuan.buslibrary.ThreadMode;

import java.lang.reflect.Method;

public class SubscribleMethod {
    //回调方法
    private Method method;
    //线程模式
    private ThreadMode threadMode;
    //方法参数
    private Class<?> type;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> type) {
        this.method = method;
        this.threadMode = threadMode;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
