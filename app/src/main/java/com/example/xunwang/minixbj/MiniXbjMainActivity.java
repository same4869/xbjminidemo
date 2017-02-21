package com.example.xunwang.minixbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xunwang.login.LoginMainActivity;
import com.example.xunwang.pluginbase.PluginBase;
import com.example.xunwang.pluginbase.PluginInfo;
import com.example.xunwang.pluginbase.PluginManager;

public class MiniXbjMainActivity extends AppCompatActivity implements View.OnClickListener {
	private Button loginBtn;
	private Button scanwordBtn;
	private String[] plugins = { PluginBase.PLUGIN_SCANWORD };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_xbj_main);

		PluginManager.getInstance(getApplicationContext()).loadPlugins(plugins);

		initView(); 
	}

	private void initView() {
		loginBtn = (Button) findViewById(R.id.login_btn);
		scanwordBtn = (Button) findViewById(R.id.scanword_btn);

		loginBtn.setOnClickListener(this);
		scanwordBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			Intent loginIntent = new Intent(MiniXbjMainActivity.this, LoginMainActivity.class);
			startActivity(loginIntent);
			break;
		case R.id.scanword_btn:
			PluginInfo pluginInfo = PluginManager.getInstance(getApplicationContext()).getPluginInfo(
					PluginBase.PLUGIN_SCANWORD);
			Log.d("kkkkkkkk", "pluginInfo --> " + pluginInfo);
			PluginBase pluginBase = null;
			if (pluginInfo != null) {
				pluginBase = pluginInfo.pluginBase;
			}
			if (pluginBase == null) {
				Toast.makeText(getApplicationContext(), "plugin not loaded", Toast.LENGTH_SHORT).show();
				return;
			}
			pluginBase.attach(MiniXbjMainActivity.this, getSupportFragmentManager());
			pluginBase.show(R.id.plugin_layout);
			break;
		}
	}
}
