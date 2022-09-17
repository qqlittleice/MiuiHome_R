package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap

object OverlapMode : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_overlap_mode")) return
        findMethod("com.miui.home.launcher.overlay.assistant.AssistantDeviceAdapter") {
            name == "inOverlapMode"
        }.hookReturnConstant(true)

    }
}