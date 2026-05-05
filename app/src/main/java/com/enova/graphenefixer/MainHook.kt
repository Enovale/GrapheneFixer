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


        val targetClasses = listOf(
            "android.ext.settings.app.AswRestrictMemoryDynCodeLoading",
            "android.ext.settings.app.AswRestrictStorageDynCodeLoading"
        )

        val replacement = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                val appInfo = param?.args?.getOrNull(2) as? ApplicationInfo
                // Settings has to always be allowed so that it can be patched as well.
                if (appInfo?.packageName == "com.android.settings") {
                    val currentClassName = param.method?.declaringClass?.name
                    return if(currentClassName=="android.ext.settings.app.AswRestrictStorageDynCodeLoading"){
                        null;
                    }else{
                        false
                    }
                }
                // All system apps are configurable (null)
                if (appInfo != null && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                    return null
                }

                // Defer to original implementation for other apps
                return XposedBridge.invokeOriginalMethod(param?.method, param?.thisObject, param?.args)
            }
        }

        for (className in targetClasses) {
            val clazz = XposedHelpers.findClass(className, lpparam.classLoader)
            XposedBridge.hookAllMethods(clazz, "getImmutableValue", replacement)
        }
    }
}