package com.enova.graphenefixer

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android") return

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "android.ext.settings.app.AswRestrictMemoryDynCodeLoading",
                lpparam.classLoader
            ),
            "shouldAllowByDefaultToSystemPkg",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    return true
                }
            }
        )
    }
}