package com.yuk.miuiHomeR.hook

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.invokeMethodAuto
import com.yuk.miuiHomeR.mPrefsMap

object HideWidgetTitles : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_widget_hide")) return
        findMethod("com.miui.home.launcher.LauncherAppWidgetHost") {
            name == "createLauncherWidgetView" && parameterCount == 4
        }.hookAfter {
            val view = it.result as Any
            view.invokeMethodAuto("getTitleView")?.invokeMethodAuto("setVisibility", View.GONE)
        }
        findMethod("com.miui.home.launcher.Launcher") {
            name == "addMaMl" && parameterCount == 3
        }.hookAfter {
            val view = it.result as Any
            view.invokeMethodAuto("getTitleView")?.invokeMethodAuto("setVisibility", View.GONE)
        }

    }
}

