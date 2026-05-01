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
                "android.ext.settings.app.AswRestrictMemoryDynCodeLoading",
                lpparam.classLoader
            ),
            "getImmutableValue",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    val appInfo = param?.args[2] as? ApplicationInfo?

                    // Settings has to always be allowed for the following patch
                    if (appInfo != null && appInfo.packageName == "com.android.settings") {
                        return false
                    }

                    // All system apps are configurable (null)
                    if (appInfo != null && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                        param.setResult(null)
                        return null
                    }

                    return XposedBridge.invokeOriginalMethod(
                        param?.method,
                        param?.thisObject,
                        param?.args
                    )
                }
            }
        )

        // All apps are treated equal for the purposes of exploit protections
        // https://github.com/GrapheneOS/platform_packages_apps_Settings/blob/bf24a0eea505f407837ee53ebe4eec1bd073c99a/src/com/android/settings/users/AppRestrictionsFragment.java#L345
        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "com.android.settings.users.AppRestrictionsFragment",
                lpparam.classLoader
            ),
            "isPlatformSigned",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    return false
                }
            }
        )
    }
}