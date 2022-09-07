package com.yuk.miuiHomeR.hook

import android.view.View
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.getBooleanField
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod

object CloseFolderWhenLaunchedApp : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_close_folder")) return
        "com.miui.home.launcher.Launcher".hookAfterMethod(
            "launch", "com.miui.home.launcher.ShortcutInfo", View::class.java
        ) {
            val mHasLaunchedAppFromFolder = it.thisObject.getBooleanField("mHasLaunchedAppFromFolder")
            if (mHasLaunchedAppFromFolder) it.thisObject.callMethod("closeFolder")
        }

    }
}

