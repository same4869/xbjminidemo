package com.example.xunwang.pluginbase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by xunwang on 16/8/22.
 */
public abstract class PlugFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PluginManager.getInstance(getActivity()).makePluginContext(getPluginName(), getActivity());
    }

    public Context getContext(){
        return context;
    }

    protected abstract String getPluginName();

}
