package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.findClassOrNull
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook

object FolderAnim : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_folder_anim")) return
        val value1 = mPrefsMap.getInt("home_folder_anim_1", 90).toFloat() / 100
        val value2 = mPrefsMap.getInt("home_folder_anim_2", 30).toFloat() / 100
        val value3 = mPrefsMap.getInt("home_folder_anim_3", 99).toFloat() / 100
        val value4 = mPrefsMap.getInt("home_folder_anim_4", 24).toFloat() / 100
        val mSpringAnimator = "com.miui.home.launcher.animate.SpringAnimator".findClass()
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        for (i in 47..60) {
            val launcherClass = "com.miui.home.launcher.Launcher$$i".findClassOrNull()
            if (launcherClass != null) {
                for (field in launcherClass.declaredFields) {
                    if (field.name == "val\$folderInfo") {
                        findMethod(launcherClass) {
                            name == "run"
                        }.hookMethod {
                            before {
                                hook1 = mSpringAnimator.hookBeforeMethod(
                                    "setDampingResponse", Float::class.javaPrimitiveType, Float::class.javaPrimitiveType
                                ) {
                                    it.args[0] = value1
                                    it.args[1] = value2
                                }
                            }
                            after {
                                hook1?.unhook()
                            }
                        }
                        break
                    }
                }
            }
        }

        "com.miui.home.launcher.Launcher".hookBeforeMethod("closeFolder", Boolean::class.java) {
            if (it.args[0] == true) {
                hook2 = mSpringAnimator.hookBeforeMethod(
                    "setDampingResponse", Float::class.javaPrimitiveType, Float::class.javaPrimitiveType
                ) { hookParam ->
                    hookParam.args[0] = value3
                    hookParam.args[1] = value4
                }
            }
        }
        "com.miui.home.launcher.Launcher".hookAfterMethod("closeFolder", Boolean::class.java) {
            hook2?.unhook()
        }

    }
}
