package com.example.xunwang.pluginbase;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by xunwang on 16/8/22.
 */
public class PluginManager {
    public static final String PLUGIN_DIR = "plugin";

    private static PluginManager sInstance = null;
    private static boolean isPluginTestMode = false;

    private Context appContext;
    private final Map<String, PluginInfo> mPluginMap = new HashMap<String, PluginInfo>();

    private PluginManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public static PluginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager(context);
                }
            }
        }
        return sInstance;
    }

    public Context makePluginContext(final String name, final Context outerContext) {
        if (isPluginTestMode) {
            return outerContext;
        }

        Context context = mPluginMap.get(name).context;
        if (context != null) {
            return context;
        }

        context = new ContextWrapper(outerContext) {
            @Override
            public Resources getResources() {
                return mPluginMap.get(name).resources;
            }

            @Override
            public AssetManager getAssets() {
                return mPluginMap.get(name).resources.getAssets();
            }

            @Override
            public Resources.Theme getTheme() {
                return mPluginMap.get(name).theme;
            }

            @Override
            public Object getSystemService(String name) {
                if (!Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
                    return super.getSystemService(name);
                }

                LayoutInflater inflater = (LayoutInflater) super.getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LayoutInflater proxyInflater = inflater.cloneInContext(this);

                return proxyInflater;

            }
        };

        return context;
    }

    public void loadPlugins(String[] pluginNames) {
        PluginLoader pluginLoader = new PluginLoader(appContext);
        pluginLoader.setPluginLoaderListener(new PluginLoader.PluginLoaderListener() {

            @Override
            public void loadResult(String[][] results) {
                Log.e("ljj", "loadPlugins finished");
                if (results == null) {
                    return;
                }
                // prePluginInfos(results);
                for (int i = 0; i < results.length; i++) {
                    Log.e("ljj", results[i][0] + ":" + results[i][1]);
                }
            }
        });
        pluginLoader.execute(pluginNames);
    }

    public PluginInfo getPluginInfo(String pluginName) {
        if (mPluginMap.containsKey(pluginName)) {
            return mPluginMap.get(pluginName);
        }

        String pluginPath = getPluginPath(pluginName);

        PluginInfo pluginInfo = null;
        try {
            pluginInfo = loadPluginInfo(pluginName, pluginPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pluginInfo != null) {
            mPluginMap.put(pluginName, pluginInfo);
        }
        return pluginInfo;
    }

    private String getPluginPath(String pluginName) {
        File dir = new File(appContext.getFilesDir(), PLUGIN_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File saveFile = new File(dir, pluginName + ".apk"); // pluginA.apk
        if (saveFile != null && saveFile.exists() && saveFile.isFile()) {
            saveFile.setExecutable(true);
            return saveFile.getAbsolutePath();
        }
        return null;
    }

    private PluginInfo loadPluginInfo(String pluginName, String pluginPath)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String optDir = getPluginOptDir(pluginName);
        String libDir = getPluginLibDir(pluginName);

        DexClassLoader dexClassLoader = createDexClassLoader(pluginPath, optDir, libDir);
        AssetManager assetManager = createAssetManager(pluginPath);
        Resources resources = createResources(assetManager);

        PluginInfo pluginInfo = new PluginInfo();

        pluginInfo.libDir = libDir;
        pluginInfo.loader = dexClassLoader;
        pluginInfo.resources = resources;

        Resources.Theme theme = resources.newTheme();
        theme.setTo(appContext.getTheme());
        pluginInfo.theme = theme;

        Class cls = Class.forName(getPluginLauncherName(pluginName), true, dexClassLoader);
        PluginBase pluginBase = (PluginBase) cls.newInstance();
        pluginInfo.pluginBase = pluginBase;

        return pluginInfo;
    }

    private String getPluginOptDir(String name) {
        File dir = new File(appContext.getFilesDir(), "/plugin/" + name + "/opt");

        dir.mkdirs();

        return dir.getAbsolutePath();
    }

    private String getPluginLibDir(String name) {
        File dir = new File(appContext.getFilesDir(), "/plugin/" + name + "/libs");

        dir.mkdirs();

        return dir.getAbsolutePath();
    }

    private DexClassLoader createDexClassLoader(String pluginPath, String optDir, String libDir) {
        return new DexClassLoader(pluginPath, optDir, libDir, appContext.getClassLoader());
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = appContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    private String getPluginLauncherName(String pluginName) {
        return new StringBuffer("com.example.xunwang.").append(pluginName).append(".LauncherPlugin").toString();
    }


}
