package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuiHomeR.mPrefsMap

object UnlockAnim : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_unlock_anim")) return
        findMethod("com.miui.home.launcher.compat.UserPresentAnimationCompatV12Phone") {
            name == "getSpringAnimator" && parameterCount == 6
        }.hookBefore {
            it.args[4] = 0.5f
            it.args[5] = 0.5f
        }
    }

}