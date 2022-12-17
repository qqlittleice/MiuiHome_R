package com.yuk.miuiHomeR.hook

import android.widget.GridView
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.dp2px
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllMethods
import de.robv.android.xposed.XposedHelpers

object FolderVerticalPadding : BaseHook() {
    override fun init() {
        val verticalPadding = mPrefsMap.getInt("home_folder_vertical_padding", 0)
        if (verticalPadding <= 0) return
        loadClass("com.miui.home.launcher.Folder").hookAfterAllMethods(
            "bind"
        ) {
            val mContent = XposedHelpers.getObjectField(it.thisObject, "mContent") as GridView
            if (verticalPadding > 0) mContent.verticalSpacing = dp2px(verticalPadding.toFloat())
        }
    }
}