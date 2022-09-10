package com.yuk.miuiHomeR.hook

import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod

object UnlockAnim : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_unlock_anim")) return
        val mSpringAnimator = "com.miui.home.launcher.animate.SpringAnimator".findClass()
        mSpringAnimator.hookBeforeMethod(
            "setDampingResponse", Float::class.javaPrimitiveType, Float::class.javaPrimitiveType
        ) {
            it.args[0] = 0.5f
            it.args[1] = 0.5f
        }
    }

}