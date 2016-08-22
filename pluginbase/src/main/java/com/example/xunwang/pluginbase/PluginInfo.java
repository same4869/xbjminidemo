package com.example.xunwang.pluginbase;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by xunwang on 16/8/22.
 */
public class PluginInfo {
    public String name;
    public String libDir;
    public ClassLoader loader;
    public Resources resources;
    public Resources.Theme theme;
    public PluginBase pluginBase;

    public Context context;
}
