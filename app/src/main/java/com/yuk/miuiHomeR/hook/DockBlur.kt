package com.yuk.miuiHomeR.hook

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.*
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

object DockBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_dock_blur")) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val mDockHeight = dp2px(mPrefsMap.getInt("home_dock_height", 98).toFloat())
        val mDockMargin = dp2px(mPrefsMap.getInt("home_dock_margin", 10).toFloat())
        val mDockBottomMargin = dp2px(mPrefsMap.getInt("home_dock_bottom_margin", 13).toFloat())
        val mDockCorner = dp2px(mPrefsMap.getInt("home_dock_corner", 25).toFloat())
        launcherClass.hookAfterMethod("onResume") {
            val mSearchBarContainer = it.thisObject.callMethod("getSearchBarContainer") as FrameLayout
            val mSearchEdgeLayout = mSearchBarContainer.parent as FrameLayout
            val blur = BlurFrameLayout(mSearchBarContainer.context)
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mDockHeight)
            lp.gravity = Gravity.BOTTOM
            lp.setMargins(mDockMargin, 0, mDockMargin, mDockBottomMargin)
            blur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(mDockCorner.toFloat())
                blurRadius = mPrefsMap.getInt("home_blur_radius", 100)
            }
            blur.layoutParams = lp
            mSearchEdgeLayout.addView(blur, 0)
            launcherClass.hookAfterMethod("isFolderShowing") { hookParam ->
                var showEditPanel = false
                val isFolderShowing = hookParam.result as Boolean
                launcherClass.hookAfterMethod("showEditPanel", Boolean::class.java) { hookParam1 ->
                    showEditPanel = hookParam1.args[0] as Boolean
                    if (showEditPanel && isFolderShowing && blur.visibility == View.VISIBLE) blur.visibility = View.GONE
                    else if (!isFolderShowing && !showEditPanel && blur.visibility == View.GONE) blur.visibility = View.VISIBLE
                }
                if ((isFolderShowing || showEditPanel) && blur.visibility == View.VISIBLE) blur.visibility = View.GONE
                else if (!isFolderShowing && !showEditPanel && blur.visibility == View.GONE) blur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("onStateSetStart", "com.miui.home.launcher.LauncherState".findClass()) { hookParam ->
                blur(blur, hookParam)
            }
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginTop", Context::class.java, Boolean::class.javaPrimitiveType
        ) { hookParam ->
            hookParam.result = dp2px(mPrefsMap.getInt("home_dock_top_margin", 30).toFloat())
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginBottom", Context::class.java, Boolean::class.java, Boolean::class.java
        ) { hookParam ->
            hookParam.result = dp2px(mPrefsMap.getInt("home_dock_icon_bottom_margin", -8).toFloat())
        }
    }

    private fun blur(blur: BlurFrameLayout, hookParam: MethodHookParam) {
        if ("LauncherState" == hookParam.args[0].javaClass.simpleName && blur.visibility == View.VISIBLE) {
            blur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                blurRadius = mPrefsMap.getInt("home_blur_radius", 100)
            }
        } else {
            blur.blurController.apply {
                backgroundColour = Color.TRANSPARENT
                blurRadius = 0
            }
        }
    }
}