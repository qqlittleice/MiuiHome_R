package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuiHomeR.mPrefsMap

object AnimDurationRatio : BaseHook() {
    override fun init() {

        val value = mPrefsMap.getInt("home_anim_ratio", 100).toFloat() / 100f
        if (value == 1f) return
        findMethod("com.miui.home.recents.util.RectFSpringAnim") {
            name == "getModifyResponse"
        }.hookBefore {
            it.result = it.args[0] as Float * value
        }
    }

}

