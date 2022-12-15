package com.yuk.miuiHomeR.hook

import android.app.Activity
import android.os.Bundle
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callStaticMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod

object ShortcutBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_shortcut_blur")) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        launcherClass.hookAfterMethod(
            "onCreate", Bundle::class.java
        ) {
            val activity = it.thisObject as Activity
            launcherClass.hookAfterMethod("isInShortcutMenuState") { hookParam ->
                val isInShortcutMenuState = hookParam.result as Boolean
                if (isInShortcutMenuState) {
                    blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true)
                }
            }
        }
    }
}
