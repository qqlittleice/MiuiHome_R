package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuiHomeR.MainHook
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.PrefsUtils

object AllowMoveAllWidgetToMinus : BaseHook() {
    override fun init() {

        if (mPrefsMap.getBoolean("home_widget_to_minus")) {
            findMethod("com.miui.home.launcher.Workspace") {
                name == "canDragToPa"
            }.hookBefore {
                val currentDragObject = it.thisObject.getObjectOrNull("mDragController")?.invokeMethodAuto("getCurrentDragObject")
                val dragInfo = currentDragObject?.invokeMethodAuto("getDragInfo")
                dragInfo?.putObject("isMIUIWidget", true)
            }
        }

        /*if (!PrefsUtils.mSharedPreferences.getBoolean("prefs_key_home_widget_to_minus", false)) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "canDragToPa"
        }.hookBefore {
            val currentDragObject = it.thisObject.getObjectOrNull("mDragController")?.invokeMethodAuto("getCurrentDragObject")
            val dragInfo = currentDragObject?.invokeMethodAuto("getDragInfo")
            dragInfo?.putObject("isMIUIWidget", true)
        }*/
    }
}

