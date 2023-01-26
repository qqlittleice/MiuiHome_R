package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap

object HideStatusBarWhenEnterRecent : BaseHook() {
    override fun init() {

        if (mPrefsMap.getBoolean("home_hide_status")) {
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isHideStatusBarWhenEnterRecents"
            }.hookReturnConstant(true)
            findMethod("com.miui.home.launcher.DeviceConfig") {
                name == "keepStatusBarShowingForBetterPerformance"
            }.hookReturnConstant(false)
        } else {
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isHideStatusBarWhenEnterRecents"
            }.hookReturnConstant(false)
        }

    }
}
