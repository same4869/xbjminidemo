package com.example.xunwang.pluginbase;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by xunwang on 16/8/22.
 */
public abstract class PluginBase {
    public static final String PLUGIN_SCANWORD = "p_scanword";

    private static Context mContext;
    private FragmentManager mFragmentManager;
    private Context pluginContext;

    public void attach(Context context, FragmentManager manager) {
        mContext = context.getApplicationContext();
        mFragmentManager = manager;

        init();
    }

    public Context getHostContext() {
        return mContext;
    }

    public Context getPluginContext(){
        if(pluginContext != null){
            return pluginContext;
        }
        pluginContext = PluginManager.getInstance(mContext).makePluginContext(getPluginName(), mContext);
        return pluginContext;
    }

    protected FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    protected FragmentTransaction getFragmentTransaction(){
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager.beginTransaction();
    }

    public abstract String getPluginName();
    public abstract void init();
    public abstract void show(int containerId);
    public abstract void hide();

    public abstract void detach();

}
