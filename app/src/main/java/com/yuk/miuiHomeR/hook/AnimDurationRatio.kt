package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap

object AnimDurationRatio : BaseHook() {
    override fun init() {

        val value = mPrefsMap.getInt("prefs_key_home_anim_ratio", 100)
        if (value == 100) return
        findMethod("com.miui.home.recents.TransitionAnimDurationHelper") {
            name == "getAnimDurationRatio"
        }.hookReturnConstant(value.toFloat() / 100)
    }

}

