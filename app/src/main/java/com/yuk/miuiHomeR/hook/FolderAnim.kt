package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook


object FolderAnim : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_folder_anim")) return
        var hook: XC_MethodHook.Unhook? = null
        try {
            findMethod("com.miui.home.launcher.Launcher$49") {
                name == "run"
            }
        } catch (e: Exception) {
            findMethod("com.miui.home.launcher.Launcher$47") {
                name == "run"
            }
        }.hookMethod {
            before {
                val mSpringAnimator = "com.miui.home.launcher.animate.SpringAnimator".findClass()
                hook = mSpringAnimator.hookBeforeMethod(
                    "setDampingResponse", Float::class.javaPrimitiveType, Float::class.javaPrimitiveType
                ) {
                    it.args[0] = 0.5f
                    it.args[1] = 0.5f
                }
            }
            after {
                hook?.unhook()
            }
        }
    }
}