package com.yuk.miuiHomeR.hook

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectFieldAs
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

object EnableFolderIconBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("small_folder_blur")) return
        val value = mPrefsMap.getInt("small_folder_corner", 60).toFloat()
        val value1 = mPrefsMap.getInt("small_folder_side", 250)
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val launcherStateClass = "com.miui.home.launcher.LauncherState".findClass()
        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
        findMethod(
            loadClass("com.miui.home.launcher.FolderIcon"), true
        ) { name == "onFinishInflate" }.hookAfter { hookParam ->
            val mIconImageView = hookParam.thisObject.getObjectFieldAs<ImageView>("mIconImageView")
            val mIconContainer = mIconImageView.parent as FrameLayout
            val blur = BlurFrameLayout(mIconContainer.context)
            val view = FrameLayout(mIconImageView.context)
            blur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(value)
            }
            mIconContainer.removeView(mIconImageView)
            blur.addView(view)
            mIconContainer.addView(blur, 0)
            val lp1 = blur.layoutParams as FrameLayout.LayoutParams
            lp1.gravity = Gravity.CENTER
            lp1.height = value1
            lp1.width = value1
            launcherClass.hookAfterMethod(
                "showEditPanel", Boolean::class.java
            ) {
                val isShowEditPanel = it.args[0] as Boolean
                if (isShowEditPanel && blur.visibility == View.VISIBLE) {
                    blur.visibility = View.GONE
                    mIconContainer.addView(mIconImageView)
                } else {
                    blur.visibility = View.VISIBLE
                    mIconContainer.removeView(mIconImageView)
                }
            }
            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                blur.visibility = View.GONE
            }
            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                blur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("onStateSetStart", launcherStateClass) {
                if ("LauncherState" == it.args[0].javaClass.simpleName) blur.visibility = View.VISIBLE
                else blur.visibility = View.GONE
            }
        }
    }

}