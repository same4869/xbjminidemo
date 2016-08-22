package com.example.xunwang.pluginbase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by xunwang on 16/8/22.
 */
public class PluginLoader extends AsyncTask<String, Void, String[][]> {
	public static final String PLUGIN_DIR = "plugin";
	private Context mContext;
	private PluginLoaderListener pluginLoaderListener;

	public interface PluginLoaderListener {
		public void loadResult(String[][] result);
	}

	public void setPluginLoaderListener(PluginLoaderListener pluginLoaderListener) {
		this.pluginLoaderListener = pluginLoaderListener;
	}

	public PluginLoader(Context context) {
		this.mContext = context;
	}

	@Override
	protected String[][] doInBackground(String... params) {
		if (params == null || params.length == 0) {
			return null;
		}
		String[][] results = new String[params.length][2];
		for (int i = 0; i < params.length; i++) {
			String apkPath = null;
			try {
				apkPath = loadPlugin(params[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			results[i][0] = params[i];
			results[i][1] = apkPath;
		}
		return results;
	}

	private String loadPlugin(String plugName) throws Exception {
		File dir = new File(mContext.getFilesDir(), PLUGIN_DIR);
		dir.mkdirs();

		File saveFile = new File(dir, plugName + ".apk"); // pluginA.apk

		saveFile.setExecutable(true);

		FileOutputStream outputStream = null;
		InputStream inputStream = null;

		try {
			int count = 0;
			byte[] buf = new byte[1024];

			inputStream = mContext.getAssets().open(plugName + ".apk");
			outputStream = new FileOutputStream(saveFile);

			while ((count = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, count);
			}

		} finally {
			inputStream.close();
			outputStream.close();
		}

		Log.e("lqp", "load pulgin write: " + saveFile.length());

		return saveFile.getAbsolutePath();
	}

	@Override
	protected void onPostExecute(String[][] result) {
		if (pluginLoaderListener != null) {
			pluginLoaderListener.loadResult(result);
		}
	}
}
