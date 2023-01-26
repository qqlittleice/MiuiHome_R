package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuiHomeR.mPrefsMap

object AnimDurationRatio : BaseHook() {
    override fun init() {

        val value1 = mPrefsMap.getInt("home_anim_ratio", 150).toFloat() / 100f
        val value2 = mPrefsMap.getInt("home_anim_ratio_recent", 130).toFloat() / 100f
        findMethod("com.miui.home.recents.util.RectFSpringAnim") {
            name == "getModifyResponse"
        }.hookBefore {
            it.result = it.args[0] as Float * value1
        }

        findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
            name == "getDeviceLevelTransitionAnimRatio"
        }.hookBefore {
            it.result = value2
        }

    }
}
