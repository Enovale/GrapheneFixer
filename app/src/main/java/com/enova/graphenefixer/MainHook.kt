package com.enova.graphenefixer

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
            "getImmutableValue",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(p0: MethodHookParam?): Any {
                    return false
                }
            }
        )
    }
}