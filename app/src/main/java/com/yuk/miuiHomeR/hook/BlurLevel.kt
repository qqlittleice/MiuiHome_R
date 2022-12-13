package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod

object BlurLevel : BaseHook() {
    override fun init() {
        val blurLevel = mPrefsMap.getStringAsInt("recent_blur", 2)
        if (blurLevel == 4) {
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "getBlurType"
            }.hookReturnConstant(0)
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "isUseCompleteBlurOnDev"
            }.hookReturnConstant(false)
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isUseSimpleAnim") {
                it.result = true
            }
        } else {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isUseSimpleAnim") {
                it.result = false
            }
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "getBlurType"
            }.hookBefore {
                when (blurLevel) {
                    0 -> it.result = 2
                    2 -> it.result = 1
                    3 -> it.result = 0
                }
            }
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "isUseCompleteBlurOnDev"
            }.hookBefore {
                when (blurLevel) {
                    1 -> it.result = true
                }
            }
        }

    }
}

