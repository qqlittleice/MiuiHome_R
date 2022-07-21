package com.yuk.miuiHomeR.hook

import android.graphics.RectF
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callStaticMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.replaceMethod

object TaskViewVertical : BaseHook() {

    override fun init() {
        val value = mPrefsMap.getInt("task_view_vertical", 100).toFloat() / 100
        if (value == -1f || value == 1f) return
        "com.miui.home.recents.views.TaskStackViewsAlgorithmVertical".replaceMethod(
            "scaleTaskView", RectF::class.java
        ) {
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod(
                "scaleRectAboutCenter",
                it.args[0],
                value * "com.miui.home.recents.util.Utilities".findClass()
                    .callStaticMethod("getTaskViewScale", appContext) as Float
            )
        }
    }
}