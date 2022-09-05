package com.yuk.miuiHomeR.hook

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorAfter
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.*
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XposedHelpers

object Dock : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_dock_blur")) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
        val mDockHeight = dp2px(mPrefsMap.getInt("home_dock_height", 98).toFloat())
        val mDockMargin = dp2px(mPrefsMap.getInt("home_dock_margin", 10).toFloat())
        val mDockBottomMargin = dp2px(mPrefsMap.getInt("home_dock_bottom_margin", 13).toFloat())
        val mDockCorner = dp2px(mPrefsMap.getInt("home_dock_corner", 25).toFloat())
        hookAllConstructorAfter("com.miui.home.launcher.Launcher") {
            var mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur")
            if (mDockBlur != null) return@hookAllConstructorAfter
            mDockBlur = BlurFrameLayout(appContext)
            XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDockBlur", mDockBlur)
        }
        launcherClass.hookAfterMethod("onCreate", Bundle::class.java) {
            val mSearchBarContainer = it.thisObject.callMethod("getSearchBarContainer") as FrameLayout
            val mSearchEdgeLayout = mSearchBarContainer.parent as FrameLayout
            val mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur") as BlurFrameLayout
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mDockHeight)
            lp.gravity = Gravity.BOTTOM
            lp.setMargins(mDockMargin, 0, mDockMargin, mDockBottomMargin)
            mDockBlur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(mDockCorner.toFloat())
                blurRadius = mPrefsMap.getInt("home_blur_radius", 100)
            }
            mDockBlur.layoutParams = lp
            mSearchEdgeLayout.addView(mDockBlur, 0)
            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                if (mDockBlur.visibility == View.VISIBLE) mDockBlur.visibility = View.GONE
            }
            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                if (mDockBlur.visibility == View.GONE) mDockBlur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("showEditPanel", Boolean::class.java) {
                if (it.args[0] as Boolean) mDockBlur.visibility = View.GONE
                else mDockBlur.visibility = View.VISIBLE
            }
        }
        launcherClass.hookAfterMethod("onStateSetStart", "com.miui.home.launcher.LauncherState".findClass()) {
            val mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur") as BlurFrameLayout
            if (mDockBlur.visibility != View.GONE && "LauncherState" == it.args[0].javaClass.simpleName) {
                mDockBlur.blurController.apply {
                    backgroundColour = Color.parseColor("#44FFFFFF")
                    blurRadius = mPrefsMap.getInt("home_blur_radius", 100)
                }
            } else {
                mDockBlur.blurController.apply {
                    backgroundColour = Color.TRANSPARENT
                    blurRadius = 0
                }
            }
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginTop", Context::class.java, Boolean::class.javaPrimitiveType
        ) {
            it.result = dp2px(mPrefsMap.getInt("home_dock_top_margin", 30).toFloat())
        }
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
            "calcHotSeatsMarginBottom", Context::class.java, Boolean::class.java, Boolean::class.java
        ) {
            it.result = dp2px(mPrefsMap.getInt("home_dock_icon_bottom_margin", -8).toFloat())
        }
    }

}