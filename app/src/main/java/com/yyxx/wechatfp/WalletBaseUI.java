package com.yyxx.wechatfp;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


public class WalletBaseUI implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    public void initZygote(StartupParam startupParam) throws Throwable {
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(WECHAT_PACKAGENAME)) {
            XposedBridge.log("loaded: [" + lpparam.packageName + "]" + " version:" + BuildConfig.VERSION_NAME);
            XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @TargetApi(21)
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Application onCreate");
                    Context context = (Context) param.thisObject;
                    XposedPluginLoader.load(XposedPlugin.class, context, lpparam);
                }
            });
        }
    }
}
