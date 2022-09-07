package com.yuk.miuiHomeR.hook

import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorAfter
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectFieldAs
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XposedHelpers

@RequiresApi(Build.VERSION_CODES.S)
object EnableFolderIconBlur : BaseHook() {
    override fun init() {
        if (!mPrefsMap.getBoolean("small_folder_blur")) return
        val value = mPrefsMap.getInt("small_folder_corner", 60).toFloat()
        val value1 = mPrefsMap.getInt("small_folder_side", 250)
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val launcherStateClass = "com.miui.home.launcher.LauncherState".findClass()
        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
        hookAllConstructorAfter("com.miui.home.launcher.FolderIcon") {
            var mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur")
            if (mDockBlur != null) return@hookAllConstructorAfter
            mDockBlur = BlurFrameLayout(InitFields.appContext)
            XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDockBlur", mDockBlur)
        }
        findMethod(
            loadClass("com.miui.home.launcher.FolderIcon"), true
        ) { name == "onFinishInflate" }.hookAfter {
            val mIconImageView = it.thisObject.getObjectFieldAs<ImageView>("mIconImageView")
            val mIconContainer = mIconImageView.parent as FrameLayout
            val mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur") as BlurFrameLayout
            val view = FrameLayout(mIconImageView.context)
            mDockBlur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(value)
            }
            mIconContainer.removeView(mIconImageView)
            mDockBlur.addView(view)
            mIconContainer.addView(mDockBlur, 0)
            val lp1 = mDockBlur.layoutParams as FrameLayout.LayoutParams
            lp1.gravity = Gravity.CENTER
            lp1.height = value1
            lp1.width = value1
            launcherClass.hookAfterMethod("showEditPanel", Boolean::class.java) { hookParam ->
                val isShowEditPanel = hookParam.args[0] as Boolean
                if (isShowEditPanel && mDockBlur.visibility == View.VISIBLE) {
                    mDockBlur.visibility = View.GONE
                    mIconContainer.addView(mIconImageView)
                } else {
                    mDockBlur.visibility = View.VISIBLE
                    mIconContainer.removeView(mIconImageView)
                }
            }
            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                mDockBlur.visibility = View.GONE
            }
            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                mDockBlur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("onStateSetStart", launcherStateClass) { hookParam ->
                if ("LauncherState" == hookParam.args[0].javaClass.simpleName) mDockBlur.visibility = View.VISIBLE
                else mDockBlur.visibility = View.GONE
            }
        }
    }

}