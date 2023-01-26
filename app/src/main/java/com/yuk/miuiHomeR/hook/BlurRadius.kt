package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookBeforeAllMethods

object BlurRadius : BaseHook() {
    override fun init() {

        val value = mPrefsMap.getInt("home_blur_radius", 100).toFloat() / 100
        if (value == 1f) return
        val blurUtilsClass = loadClass("com.miui.home.launcher.common.BlurUtils")
        blurUtilsClass.hookBeforeAllMethods("fastBlur") {
            it.args[0] = it.args[0] as Float * value
        }

    }
}
