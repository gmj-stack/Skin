package com.lib.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.collection.ArrayMap;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * @author gmj
 * 监听生命周期，替换我们自己的布局加载器
 */
public class ApplicationActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Observable mObserable;
    private ArrayMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactories = new ArrayMap<>();
    private SkinLayoutInflaterFactory skinLayoutInflaterFactory;

    public
    ApplicationActivityLifecycle(Observable observable) {
        mObserable = observable;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        /**
         *  更新布局视图
         */
        // 获得Activity的布局加载器
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        if (Build.VERSION.SDK_INT <= 28) {
            //  针对 android版本9及一下
            try {
                // Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
                // 如设置过抛出一次
                // 设置 mFactorySet 标签为false
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(layoutInflater, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //            使用factory2 设置布局加载工程
            skinLayoutInflaterFactory = new SkinLayoutInflaterFactory();
            LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        } else {
            // 针对android9以上
            Class<LayoutInflater> inflaterClass = LayoutInflater.class;
            try {
                Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
                mFactory2.setAccessible(true);
                // 使用factory2 设置布局加载工程
                skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(  );
                mFactory2.set(layoutInflater, skinLayoutInflaterFactory);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);

        mObserable.addObserver(skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }
}
