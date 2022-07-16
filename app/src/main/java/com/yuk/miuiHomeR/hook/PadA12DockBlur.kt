package com.yuk.miuiHomeR.hook

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.checkIsPadDevice
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XposedHelpers

object PadA12DockBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("pad_dock_blur") || Build.VERSION.SDK_INT < 31 || !checkIsPadDevice()) return
        try {
            "com.miui.home.launcher.Launcher".hookAfterMethod("onCreate", Bundle::class.java) {
                val activity = it.thisObject as Activity
                val view = activity.findViewById(
                    activity.resources.getIdentifier("hotseat_background", "id", "com.miui.home")
                ) as ViewGroup
                val blur = BlurFrameLayout(view.context)
                blur.blurController.apply {
                    backgroundColour = Color.parseColor("#44FFFFFF")
                    cornerRadius = CornersRadius.all(40f)
                }
                view.addView(blur)
            }
        } catch (e: XposedHelpers.ClassNotFoundError) {
            Log.ex(e)
        }
    }
}