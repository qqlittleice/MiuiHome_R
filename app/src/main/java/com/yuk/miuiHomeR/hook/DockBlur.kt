package com.yuk.miuiHomeR.hook

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import moralnorm.utils.DisplayUtils

object DockBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_dock_blur")) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val launcherStateManagerClass = "com.miui.home.launcher.LauncherStateManager".findClass()
        launcherClass.hookAfterMethod(
            "onCreate", Bundle::class.java
        ) {
            var isInEditing = false
            var isFolderShowing = false
            val mSearchBarContainer =
                it.thisObject.callMethod("getSearchBarContainer") as FrameLayout
            val mSearchEdgeLayout = mSearchBarContainer.parent as FrameLayout
            val mDockHeight = DisplayUtils.dip2px(
                mSearchBarContainer.context, mPrefsMap.getInt("home_dock_height", 85).toFloat()
            )
            val mDockMargin = DisplayUtils.dip2px(
                mSearchBarContainer.context, mPrefsMap.getInt("home_dock_margin", 10).toFloat()
            )
            val mDockBottomMargin = DisplayUtils.dip2px(
                mSearchBarContainer.context,
                mPrefsMap.getInt("home_dock_bottom_margin", 30).toFloat()
            )
            val mDockCorner = DisplayUtils.dip2px(
                mSearchBarContainer.context, mPrefsMap.getInt("home_dock_corner", 50).toFloat()
            )
            val blur = BlurFrameLayout(mSearchBarContainer.context)
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mDockHeight)
            lp.gravity = Gravity.BOTTOM
            lp.setMargins(mDockMargin, 0, mDockMargin, mDockBottomMargin)
            blur.layoutParams = lp
            mSearchEdgeLayout.addView(blur, 0)
            launcherClass.hookAfterMethod("isFolderShowing") { hookParam ->
                isFolderShowing = hookParam.result as Boolean
                if (isFolderShowing) blur.visibility = View.GONE
                else blur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("isInEditing") { hookParam ->
                isInEditing = hookParam.result as Boolean
            }
            launcherStateManagerClass.hookAfterMethod("getState") { hookParam ->
                val state = hookParam.result.toString()
                val a = state.lastIndexOf("LauncherState")
                if (a != -1 && !isInEditing) {
                    blur.blurController.apply {
                        backgroundColour = Color.parseColor("#44FFFFFF")
                        cornerRadius = CornersRadius.all(mDockCorner.toFloat())
                        blurRadius = 100
                    }
                } else {
                    blur.blurController.apply {
                        backgroundColour = Color.TRANSPARENT
                        blurRadius = 0
                    }
                }
            }
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginTop", Context::class.java, Boolean::class.javaPrimitiveType
        ) {
            val context: Context = it.args[0] as Context
            it.result = DisplayUtils.dip2px(
                context, mPrefsMap.getInt("home_dock_top_margin", 30).toFloat()
            )
        }
    }
}
