package com.yuk.miuiHomeR.hook

import android.view.View
import android.view.ViewGroup
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.getObjectField
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod

object HideSeekPoint : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_hide_seek_point")) return
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "updateSeekPoints", Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "addView", View::class.java, Int::class.javaPrimitiveType, ViewGroup.LayoutParams::class.java
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "removeScreen", Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "removeScreensInLayout", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
    }

    private fun showSeekBar(view: View) {
        if ("Workspace" != view.javaClass.simpleName) return
        val mScreenSeekBar = view.getObjectField("mScreenSeekBar") as View
        val isInEditingMode = view.callMethod("isInNormalEditingMode") as Boolean
        if (!isInEditingMode) {
            mScreenSeekBar.animate().cancel()
            mScreenSeekBar.visibility = View.GONE
        }

    }
}
