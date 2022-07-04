package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap

object AlwaysShowStatusClock : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_show_status_clock")) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "isScreenHasClockGadget" && parameterCount == 1
        }.hookReturnConstant(false)
    }

}

