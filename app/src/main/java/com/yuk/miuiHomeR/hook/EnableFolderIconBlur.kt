package com.yuk.miuiHomeR.hook

import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.getObjectFieldAs
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

object EnableFolderIconBlur : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("small_folder_blur") || Build.VERSION.SDK_INT < 31) return
        val value = mPrefsMap.getInt("small_folder_corner", 30).toFloat()
        val value1 = mPrefsMap.getInt("small_folder_side", 300)
        findMethod(loadClass("com.miui.home.launcher.FolderIcon"), true)
        { name == "onFinishInflate" }.hookAfter { hookParam ->
            val mIconImageView = hookParam.thisObject.getObjectFieldAs<ImageView>("mIconImageView")
            val mIconContainer = mIconImageView.parent as FrameLayout
            val blur = WindowBlurFrameLayout(mIconContainer.context)
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
        }

    }
}