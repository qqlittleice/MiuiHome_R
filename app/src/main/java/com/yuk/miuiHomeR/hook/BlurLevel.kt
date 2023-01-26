package com.yuk.miuiHomeR.hook

import android.app.Activity
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callStaticMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllMethods
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
        if (blurLevel == 0 && mPrefsMap.getBoolean("complete_blur_fix")) {
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val navStubViewClass = "com.miui.home.recents.NavStubView".findClass()
            val applicationClass = "com.miui.home.launcher.Application".findClass()
            navStubViewClass.hookAfterAllMethods("onTouchEvent") {
                val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
                blurClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 500L)
            }
        }

    }
}
