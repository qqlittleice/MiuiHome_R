package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuiHomeR.utils.PrefsUtils
import de.robv.android.xposed.XC_MethodHook

object AlwaysShowMiuiWidget : BaseHook() {
    override fun init() {
        if (!PrefsUtils.mSharedPreferences.getBoolean("prefs_key_home_widget_to_minus", false)) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
            name == "buildAppWidgetsItems" && parameterCount == 2
        }.hookBefore {
            hook1 = findMethod("com.miui.home.launcher.LauncherAppWidgetProviderInfo") {
                name == "fromProviderInfo" && parameterCount == 2
            }.hookAfter {
                it.thisObject.apply {
                    putObject("isMIUIWidget", false)
                    getObjectOrNull("providerInfo")?.putObject("widgetFeatures", 0)
                }
            }
            hook2 = findMethod("com.miui.home.launcher.MIUIWidgetUtil") {
                name == "isMIUIWidgetSupport"
            }.hookReturnConstant(false)
        }
        findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
            name == "buildAppWidgetsItems" && parameterCount == 2
        }.hookAfter {
            hook1?.unhook()
            hook2?.unhook()
        }
    }
}

