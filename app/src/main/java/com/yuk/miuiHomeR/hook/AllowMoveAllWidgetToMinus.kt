package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod

object AllowMoveAllWidgetToMinus : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_widget_to_minus")) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "canDragToPa"
        }.hookBefore {
            val currentDragObject = it.thisObject.getObjectOrNull("mDragController")?.invokeMethodAuto("getCurrentDragObject")
            val dragInfo = currentDragObject?.invokeMethodAuto("getDragInfo")
            val i = dragInfo?.getObject("spanX")
            val launcherCallbacks = it.thisObject.getObjectOrNull("mLauncher")?.invokeMethodAuto("getLauncherCallbacks")
            val isDraggingFromAssistant = it.thisObject.getObjectOrNull("mDragController")?.callMethod("isDraggingFromAssistant") as Boolean
            val isDraggingToAssistant = it.thisObject.getObjectOrNull("mDragController")?.callMethod("isDraggingToAssistant") as Boolean
            it.result = launcherCallbacks != null && !isDraggingFromAssistant && !isDraggingToAssistant && i != 1
        }

    }
}

