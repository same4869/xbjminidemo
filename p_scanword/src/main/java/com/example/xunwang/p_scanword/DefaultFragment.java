package com.example.xunwang.p_scanword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xunwang.pluginbase.PlugFragment;
import com.example.xunwang.pluginbase.PluginBase;
import com.example.xunwang.scanword.R;

/**
 * Created by xunwang on 16/8/22.
 */
public class DefaultFragment extends PlugFragment {
	@Override
	protected String getPluginName() {
		return PluginBase.PLUGIN_SCANWORD;
	}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        ViewGroup viewGroup = (ViewGroup) factory.inflate(R.layout.pla_linear, container, false);
        return viewGroup;
    }
}
