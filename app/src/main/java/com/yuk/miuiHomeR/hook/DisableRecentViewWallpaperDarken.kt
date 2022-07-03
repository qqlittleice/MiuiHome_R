package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.yuk.miuiHomeR.utils.PrefsUtils

object DisableRecentViewWallpaperDarken : BaseHook() {
    override fun init() {
        if (!PrefsUtils.mSharedPreferences.getBoolean("prefs_key_home_disable_darken", false)) return
        findMethod("com.miui.home.recents.DimLayer") {
            name == "dim" && parameterCount == 3
        }.hookBefore {
            it.args[0] = 0.0f
            it.thisObject.putObject("mCurrentAlpha", 0.0f)
        }
    }
}

