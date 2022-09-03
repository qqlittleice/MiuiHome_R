package com.yuk.miuiHomeR.hook

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.*
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

object DockBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_dock_blur")) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        launcherClass.hookAfterMethod(
            "onResume"
        ) {
            val mSearchBarContainer =
                it.thisObject.callMethod("getSearchBarContainer") as FrameLayout
            val mSearchEdgeLayout = mSearchBarContainer.parent as FrameLayout
            val mDockHeight = dp2px(mPrefsMap.getInt("home_dock_height", 98).toFloat())
            val mDockMargin = dp2px(mPrefsMap.getInt("home_dock_margin", 10).toFloat())
            val mDockBottomMargin = dp2px(mPrefsMap.getInt("home_dock_bottom_margin", 13).toFloat())
            val mDockCorner = dp2px(mPrefsMap.getInt("home_dock_corner", 25).toFloat())
            val blur = BlurFrameLayout(mSearchBarContainer.context)
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mDockHeight)
            lp.gravity = Gravity.BOTTOM
            blur.blurController.cornerRadius = CornersRadius.all(mDockCorner.toFloat())
            lp.setMargins(mDockMargin, 0, mDockMargin, mDockBottomMargin)
            blur.layoutParams = lp
            mSearchEdgeLayout.addView(blur, 0)
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginTop", Context::class.java, Boolean::class.javaPrimitiveType
        ) { hookParam ->
            hookParam.result = dp2px(mPrefsMap.getInt("home_dock_top_margin", 30).toFloat())
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginBottom",
            Context::class.java,
            Boolean::class.java,
            Boolean::class.java
        ) { hookParam ->
            hookParam.result = dp2px(mPrefsMap.getInt("home_dock_icon_bottom_margin", -8).toFloat())
        }
    }
}
