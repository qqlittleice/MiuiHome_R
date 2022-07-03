package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.utils.PrefsUtils

object AlwaysShowStatusClock : BaseHook() {
    override fun init() {
        if (!PrefsUtils.mSharedPreferences.getBoolean("prefs_key_home_show_status_clock", false)) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "isScreenHasClockGadget" && parameterCount == 1
        }.hookReturnConstant(false)
    }
}

