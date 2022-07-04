package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuiHomeR.mPrefsMap

object AllowMoveAllWidgetToMinus : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_widget_to_minus")) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "canDragToPa"
        }.hookBefore {
            val currentDragObject = it.thisObject.getObjectOrNull("mDragController")
                ?.invokeMethodAuto("getCurrentDragObject")
            val dragInfo = currentDragObject?.invokeMethodAuto("getDragInfo")
            dragInfo?.putObject("isMIUIWidget", true)
        }

    }
}

