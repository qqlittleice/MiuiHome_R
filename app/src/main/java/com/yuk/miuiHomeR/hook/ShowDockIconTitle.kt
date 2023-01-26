package com.yuk.miuiHomeR.hook

import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod

object ShowDockIconTitle : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_dock_icon_title")) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isHotseatsAppTitleHided") {
            it.result = false
        }

    }
}
