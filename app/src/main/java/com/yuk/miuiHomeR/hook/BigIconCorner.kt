package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuiHomeR.mPrefsMap

object BigIconCorner : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("big_icon_corner")) return
        findMethod("com.miui.home.launcher.bigicon.BigIconUtil") {
            name == "getCroppedFromCorner" && parameterCount == 4
        }.hookBefore {
            it.args[0] = 2
            it.args[1] = 2
        }
    }
}