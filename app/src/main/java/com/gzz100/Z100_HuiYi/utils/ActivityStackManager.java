package com.gzz100.Z100_HuiYi.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.util.Log;


import java.util.LinkedList;

/**
 * Activity管理器
 *
 * @author Leon Tung
 * @version 1.0
 * @date 2015-8-10.
 */
public class ActivityStackManager {

    //Activity管理器实例
    private static ActivityStackManager mInstance = new ActivityStackManager();
    //Activity栈
    private LinkedList<Activity> mActivityStack = new LinkedList<>();

    //私有化构造函数
    private ActivityStackManager() {
    }

    /**
     * 显示当前Activity栈内元素
     */
    public static void showStack() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mInstance.mActivityStack.size(); i++) {
            if (mInstance.mActivityStack.size() - 1 == i) {
                sb.append("[" + i + "] : " + mInstance.mActivityStack.get(i).getLocalClassName());
                continue;
            }
            sb.append("[" + i + "] : " + mInstance.mActivityStack.get(i).getLocalClassName() + ", ");
        }
        if (mInstance.mActivityStack.size() > 0) {
            return;
        }
    }

    //压入栈顶
    public static void push(Activity activity) {
        mInstance.mActivityStack.push(activity);
        showStack();
    }

    //压入栈顶,根据类名
    public static void pushMain(String className) {
        for (int i=0;i<mInstance.mActivityStack.size();i++){
            if (mInstance.mActivityStack.get(i).getComponentName().getClassName().equals(className)){
                mInstance.mActivityStack.push(mInstance.mActivityStack.get(i));
            }
        }
    }

    //移除栈顶Activity，并finish
    public static void pop() {
        Activity activity = mInstance.mActivityStack.pollFirst();
        if (null != activity) {
            activity.finish();
            activity = null;
        }
        showStack();
    }

    //清空Activity栈，并Finish全部Activity
    public static void clear() {
        Activity activity = null;
        while (true) {
            activity = mInstance.mActivityStack.pollFirst();
            if (null == activity) {
                break;
            }
            activity.finish();
            activity = null;
        }
        showStack();
    }

    //清空Activity栈，，并finish，只保留指定Activity
    public static void clearExceptOne(Activity activity) {
        while (true) {
            //移除并返回栈顶元素
            Activity temp = mInstance.mActivityStack.pollFirst();
            if (null == temp) {
                break;
            }
            if (temp == activity) {
                continue;
            } else {
                temp.finish();
            }
        }
        mInstance.mActivityStack.push(activity);
        showStack();
    }

    //移除栈内指定Activity之上的其他Activity
    public static void clearTop(Activity activity) {
        while (true) {
            Activity temp = mInstance.mActivityStack.pollFirst();
            if (null == temp || temp == activity) {
                break;
            } else {
                temp.finish();
            }
        }
        showStack();
    }

    //结束进程
    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static int stackSize() {
        return mInstance.mActivityStack.size();
    }

    public static void moveToFirst(Activity activity){
        for (int i = 0; i < mInstance.mActivityStack.size(); i++) {
            if (mInstance.mActivityStack.get(i) == activity){
                mInstance.mActivityStack.set(0,activity);
                ComponentName componentName = mInstance.mActivityStack.get(i).getComponentName();

                Log.e("ActivityStackManager","===================  "+componentName.getClassName());
            }
        }
    }

}
