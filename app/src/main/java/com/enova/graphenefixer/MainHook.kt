package com.enova.graphenefixer

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android" && lpparam.packageName != "com.android.settings") return

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "android.ext.settings.app.AswRestrictMemoryDynCodeLoading", lpparam.classLoader
            ), "getImmutableValue", object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    val appInfo = param?.args[2] as? ApplicationInfo?

                    // Settings has to always be allowed so that it can be patched as well.
                    if (appInfo != null && appInfo.packageName == "com.android.settings") {
                        return false
                    }

                    // All system apps are configurable (null)
                    if (appInfo != null && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                        return null
                    }

                    // Defer to original implementation for other apps
                    return XposedBridge.invokeOriginalMethod(
                        param?.method, param?.thisObject, param?.args
                    )
                }
            }
        )
    }
}
