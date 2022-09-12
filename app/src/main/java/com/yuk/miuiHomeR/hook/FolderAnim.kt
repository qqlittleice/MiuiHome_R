package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.*
import de.robv.android.xposed.XC_MethodHook

object FolderAnim : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_folder_anim")) return
        val value1 = mPrefsMap.getInt("home_folder_anim_1", 90).toFloat() / 100
        val value2 = mPrefsMap.getInt("home_folder_anim_2", 30).toFloat() / 100
        val value3 = mPrefsMap.getInt("home_folder_anim_3", 99).toFloat() / 100
        val value4 = mPrefsMap.getInt("home_folder_anim_4", 24).toFloat() / 100
        val mSpringAnimator = "com.miui.home.launcher.animate.SpringAnimator".findClass()
        var hook: XC_MethodHook.Unhook? = null
        for (i in 47..60) {
            val launcherClass = "com.miui.home.launcher.Launcher$$i".findClassOrNull()
            if (launcherClass != null) {
                for (field in launcherClass.declaredFields) {
                    if (field.name == "val\$folderInfo") {
                        findMethod(launcherClass) {
                            name == "run"
                        }.hookMethod {
                            before {
                                hook = mSpringAnimator.hookBeforeMethod(
                                    "setDampingResponse", Float::class.javaPrimitiveType, Float::class.javaPrimitiveType
                                ) {
                                    it.args[0] = value1
                                    it.args[1] = value2
                                }
                            }
                            after {
                                hook?.unhook()
                            }
                        }
                        break
                    }
                }
            }
        }

        "com.miui.home.launcher.Launcher".hookAfterMethod("closeFolder", Boolean::class.java) {
            if (it.args[0] == true) {
                val mFolderOpenAnim = it.thisObject.getObjectField("mFolderOpenAnim")
                mFolderOpenAnim?.callMethod("setDampingResponse", value3, value4)
                mFolderOpenAnim?.callMethod("setStartEnd", 1.0f, 0.0f)
                mFolderOpenAnim?.callMethod("start")
                it.thisObject.callMethod("fadeInOrOutScreenContentWhenFolderAnimate",false)
            }
        }

    }
}