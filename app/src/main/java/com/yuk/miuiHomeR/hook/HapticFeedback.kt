package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.getObjectField
import com.yuk.miuiHomeR.utils.ktx.replaceMethod

object HapticFeedback : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("haptic_feedback")) return
        val value = mPrefsMap.getInt("haptic_Back_HandUp", 2)
        val value1 = mPrefsMap.getInt("haptic_Back_ReadyBack", 2)
        findMethod("com.miui.home.launcher.common.HapticFeedbackCompatLinear") {
            name == "lambda\$performGestureBackHandUp\$8" && parameterCount == 1
        }.replaceMethod {
            it.args[0].getObjectField("mHapticHelper")?.callMethod("performExtHapticFeedback", value)
        }

        findMethod("com.miui.home.launcher.common.HapticFeedbackCompatLinear") {
            name == "lambda\$performGestureReadyBack\$7" && parameterCount == 1
        }.replaceMethod {
            it.args[0].getObjectField("mHapticHelper")?.callMethod("performExtHapticFeedback", value1)
        }
    }
}