package com.yuk.miuiHomeR.hook

import android.graphics.Color
import android.graphics.drawable.LayerDrawable
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
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectField
import com.yuk.miuiHomeR.utils.ktx.getObjectFieldAs
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllMethods
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.zhenxiang.blur.BlurFrameLayout
import com.zhenxiang.blur.createBackgroundBlurDrawable
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XposedHelpers

@RequiresApi(Build.VERSION_CODES.S)
object EnableBigFolderIconBlur : BaseHook() {
    override fun init() {
        if (!mPrefsMap.getBoolean("big_folder_blur")) return
        var isShowEditPanel = false
        val value = mPrefsMap.getInt("big_folder_corner", 58).toFloat()
        val value1 = mPrefsMap.getInt("big_folder_width", 650)
        val value2 = mPrefsMap.getInt("big_folder_height", 585)
        val value3 = mPrefsMap.getInt("big_folder_drag", 27)
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val launcherStateClass = "com.miui.home.launcher.LauncherState".findClass()
        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
        hookAllConstructorAfter("com.miui.home.launcher.FolderIcon") {
            var mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur")
            if (mDockBlur != null) return@hookAllConstructorAfter
            mDockBlur = BlurFrameLayout(InitFields.appContext)
            XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDockBlur", mDockBlur)
        }
        //小文件夹模糊
        try {
            findMethod(
                loadClass("com.miui.home.launcher.folder.FolderIcon2x2_4"), true
            ) { name == "onFinishInflate" }
        } catch (e: Exception) {
            findMethod(
                loadClass("com.miui.home.launcher.FolderIcon"), true
            ) { name == "onFinishInflate" }
        }.hookAfter {
            val mIconImageView = it.thisObject.getObjectFieldAs<ImageView>("mImageView")
            val mIconContainer = mIconImageView.parent as FrameLayout
            val mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur") as BlurFrameLayout
            val view = FrameLayout(mIconImageView.context)
            mDockBlur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(value)
            }
            mIconImageView.visibility = View.GONE
            mDockBlur.addView(view)
            mIconContainer.addView(mDockBlur, 0)
            val lp1 = mDockBlur.layoutParams as FrameLayout.LayoutParams
            lp1.gravity = Gravity.CENTER
            lp1.width = value1
            lp1.height = value2
            launcherClass.hookAfterMethod("showEditPanel", Boolean::class.java) { hookParam ->
                isShowEditPanel = hookParam.args[0] as Boolean
                if (isShowEditPanel) {
                    mDockBlur.visibility = View.GONE
                    mIconImageView.visibility = View.VISIBLE
                } else {
                    mDockBlur.visibility = View.VISIBLE
                    mIconImageView.visibility = View.GONE
                }
            }
            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                mDockBlur.visibility = View.GONE
            }
            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                if (!isShowEditPanel) mDockBlur.visibility = View.VISIBLE
            }
            launcherClass.hookAfterMethod("onStateSetStart", launcherStateClass) { hookParam ->
                if ("LauncherState" == hookParam.args[0].javaClass.simpleName) mDockBlur.visibility = View.VISIBLE
                else mDockBlur.visibility = View.GONE
            }
        }
        val dragViewClass = "com.miui.home.launcher.DragView".findClass()
        dragViewClass.hookAfterAllMethods("showWithAnim") {
            val dragView = it.thisObject as View
            dragView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            val mDragInfo = it.thisObject.getObjectField("mDragInfo") ?: return@hookAfterAllMethods
            val itemType = mDragInfo.getObjectField("itemType") as Int
            val mLauncher = it.thisObject.getObjectField("mLauncher")
            val isFolderShowing = mLauncher?.callMethod("isFolderShowing") as Boolean
            if (!isFolderShowing && itemType == 21) {
                val blurDrawable = dragView.createBackgroundBlurDrawable()
                blurDrawable?.callMethod("setColor", Color.parseColor("#44FFFFFF"))
                blurDrawable?.callMethod("setBlurRadius", 100)
                blurDrawable?.callMethod("setCornerRadius", value, value, value, value)
                val backgroundDrawable = LayerDrawable(arrayOf(blurDrawable))
                backgroundDrawable.setLayerHeight(0, value2)
                backgroundDrawable.setLayerWidth(0, value1)
                backgroundDrawable.setLayerGravity(0, Gravity.CENTER_HORIZONTAL)
                backgroundDrawable.setLayerInsetTop(0, value3)
                dragView.background = backgroundDrawable
            }
        }
    }
}