package com.example.xunwang.p_scanword;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.xunwang.pluginbase.PluginBase;

/**
 * Created by xunwang on 16/8/22.
 */
public class LauncherPlugin extends PluginBase {
    public static int CONTAINER_ID;
    private Fragment mFragment;

    @Override
    public String getPluginName() {
        return PLUGIN_SCANWORD;
    }

    @Override
    public void init() {
        mFragment = new DefaultFragment();
    }

    @Override
    public void show(int containerId) {
        CONTAINER_ID = containerId;
        FragmentTransaction ft = getFragmentTransaction();
        ft.replace(containerId, mFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void hide() {
        if(mFragment.getView() != null){
            mFragment.getView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        getFragmentTransaction().detach(mFragment).commit();
    }
}
