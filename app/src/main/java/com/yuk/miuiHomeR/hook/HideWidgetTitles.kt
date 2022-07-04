package com.yuk.miuiHomeR.hook

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.invokeMethodAuto
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import java.util.function.Predicate

object HideWidgetTitles : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_widget_hide")) return
        val maMlWidgetInfo = loadClass("com.miui.home.launcher.maml.MaMlWidgetInfo")
        findMethod("com.miui.home.launcher.LauncherAppWidgetHost") {
            name == "createLauncherWidgetView" && parameterCount == 4
        }.hookAfter {
            val view = it.result as Any
            view.invokeMethodAuto("getTitleView")?.invokeMethodAuto("setVisibility", View.GONE)
        }
        "com.miui.home.launcher.Launcher".hookAfterMethod(
            "addMaMl", maMlWidgetInfo, Boolean::class.java, Predicate::class.java
        ) {
            val view = it.result as Any
            view.invokeMethodAuto("getTitleView")?.invokeMethodAuto("setVisibility", View.GONE)
        }

    }
}

