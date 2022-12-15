package com.yuk.miuiHomeR.hook

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callStaticMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import com.yuk.miuiHomeR.utils.ktx.isAlpha

object EnableBlurWhenOpenFolder : BaseHook() {
    override fun init() {

        if (mPrefsMap.getStringAsInt(
                "recent_blur", 0
            ) == 4 || !mPrefsMap.getBoolean("home_folder_blur")
        ) {
            if (isAlpha()) {
                "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUserBlurWhenOpenFolder") {
                    it.result = false
                }
            }
        } else {
            if (mPrefsMap.getBoolean("home_folder_blur")) {
                if (isAlpha()) {
                    "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUserBlurWhenOpenFolder") {
                        it.result = true
                    }
                } else {
                    val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
                    val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
                    val launcherClass = "com.miui.home.launcher.Launcher".findClass()
                    val launcherStateClass = "com.miui.home.launcher.LauncherState".findClass()
                    val cancelShortcutMenuReasonClass = "com.miui.home.launcher.shortcuts.CancelShortcutMenuReason".findClass()
                    launcherClass.hookAfterMethod("onCreate", Bundle::class.java) {
                        val activity = it.thisObject as Activity
                        var isFolderShowing = false
                        var isShowEditPanel = false
                        launcherClass.hookAfterMethod("isFolderShowing") { hookParam ->
                            isFolderShowing = hookParam.result as Boolean
                        }
                        launcherClass.hookAfterMethod("showEditPanel", Boolean::class.java) { hookParam ->
                            isShowEditPanel = hookParam.args[0] as Boolean
                        }
                        launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                            blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true)
                        }
                        launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                            if (isShowEditPanel) blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            else blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, 300L)
                        }
                        launcherClass.hookAfterMethod("cancelShortcutMenu", Int::class.java, cancelShortcutMenuReasonClass) {
                            if (isFolderShowing) blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        }
                        blurClass.hookAfterMethod(
                            "fastBlurWhenStartOpenOrCloseApp", Boolean::class.java, launcherClass
                        ) { hookParam ->
                            if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            else if (isShowEditPanel) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        }
                        blurClass.hookAfterMethod(
                            "fastBlurWhenFinishOpenOrCloseApp", launcherClass
                        ) { hookParam ->
                            if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            else if (isShowEditPanel) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        }
                        blurClass.hookAfterMethod(
                            "fastBlurWhenExitRecents", launcherClass, launcherStateClass, Boolean::class.java
                        ) { hookParam ->
                            if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            else if (isShowEditPanel) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        }
                        launcherClass.hookAfterMethod("onGesturePerformAppToHome") {
                            if (isFolderShowing) blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 300L)
                        }
                    }
                }
            }
        }

    }
}