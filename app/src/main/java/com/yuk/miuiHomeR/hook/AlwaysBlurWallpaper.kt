package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuiHomeR.mPrefsMap

object AlwaysBlurWallpaper : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_blur_wallpaper")) return
        findMethod("com.miui.home.launcher.common.BlurUtils") {
            name == "fastBlur" && parameterCount == 4
        }.hookBefore {
            it.args[2] = true
        }

    }
}

