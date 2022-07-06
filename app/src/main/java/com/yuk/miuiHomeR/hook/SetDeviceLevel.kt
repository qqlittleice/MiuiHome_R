package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod

object SetDeviceLevel : BaseHook() {
    override fun init() {

        try {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("getDeviceLevel") {
                it.result = 2
            }
            "com.miui.home.launcher.common.CpuLevelUtils".hookBeforeMethod(
                "getQualcommCpuLevel", String::class.java
            ) {
                it.result = 2
            }
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isSupportCompleteAnimation") {
                it.result = true
            }
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isLowLevelOrLiteDevice") {
                it.result = false
            }
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isDefaultIcon") {
                it.result = true
            }
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isMiuiLiteVersion") {
                it.result = false
            }
            "com.miui.home.launcher.util.noword.NoWordSettingHelperKt".hookBeforeMethod("isNoWordAvailable") {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }

    }
}