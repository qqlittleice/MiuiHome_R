package com.yuk.miuiHomeR.hook

import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod

object UnlockHotseatIcon : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_unlock_hotseat")) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("getHotseatMaxCount") {
            it.result = 99
        }
    }
}