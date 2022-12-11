package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.utils.ktx.checkVersionCode
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import com.yuk.miuiHomeR.utils.ktx.replaceMethod

object SetDeviceLevel : BaseHook() {
    override fun init() {

        try {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("getDeviceLevel") {
                it.result = 2
            }
            try {
                findMethod("com.miui.home.launcher.common.CpuLevelUtils") {
                    name == "getQualcommCpuLevel" && parameterCount == 1
                }
            } catch (e: Exception) {
                findMethod("miuix.animation.utils.DeviceUtils") {
                    name == "getQualcommCpuLevel" && parameterCount == 1
                }
            }.hookReturnConstant(2)
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isSupportCompleteAnimation") {
                it.result = true
            }
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isLowLevelOrLiteDevice") {
                it.result = false
            }
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isMiuiLiteVersion") {
                it.result = false
            }
            "com.miui.home.launcher.util.noword.NoWordSettingHelperKt".hookBeforeMethod("isNoWordAvailable") {
                it.result = true
            }
            "android.os.SystemProperties".hookBeforeMethod(
                "getBoolean", String::class.java, Boolean::class.java
            ) {
                if (it.args[0] == "ro.config.low_ram.threshold_gb") it.result = false
            }
            "android.os.SystemProperties".hookBeforeMethod(
                "getBoolean", String::class.java, Boolean::class.java
            ) {
                if (it.args[0] == "ro.miui.backdrop_sampling_enabled") it.result = true
            }
            "com.miui.home.launcher.common.Utilities".hookBeforeMethod("canLockTaskView") {
                it.result = true
            }
            "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod("isMIUIWidgetSupport") {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }

        try {
            if (checkVersionCode() <= 426004312L) "com.miui.home.launcher.MiuiHomeLog".hookBeforeMethod(
                "setDebugLogState", Boolean::class.java
            ) {
                it.result = false
            }
            "com.miui.home.launcher.MiuiHomeLog".findClass().replaceMethod(
                "log", String::class.java, String::class.java
            ) {
                return@replaceMethod null
            }
            "com.xiaomi.onetrack.OneTrack".hookBeforeMethod("isDisable") {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }

    }
}
