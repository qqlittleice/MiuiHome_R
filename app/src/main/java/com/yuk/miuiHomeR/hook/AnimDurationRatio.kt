package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuiHomeR.mPrefsMap
import kotlin.math.pow

object AnimDurationRatio : BaseHook() {
    override fun init() {

        val value = mPrefsMap.getInt("home_anim_ratio", 100).toFloat() / 100f
        if (value == 1f) return
        findMethod("com.miui.home.recents.util.RectFSpringAnim") {
            name == "getModifyResponse"
        }.hookBefore {
            it.result = it.args[0] as Float * value
        }
        findMethod("com.miui.home.recents.util.RectFSpringAnim") {
            name == "reset" && parameterCount == 6
        }.hookAfter {
            val x = it.thisObject.getObject("DEFAULT_CENTER_X_STIFFNESS") as Float
            val y = it.thisObject.getObject("DEFAULT_CENTER_Y_STIFFNESS") as Float
            val width = it.thisObject.getObject("DEFAULT_WIDTH_STIFFNESS") as Float
            val ratio = it.thisObject.getObject("DEFAULT_RATIO_STIFFNESS") as Float
            val radius = it.thisObject.getObject("DEFAULT_RADIUS_STIFFNESS") as Float
            val alpha = it.thisObject.getObject("DEFAULT_ALPHA_STIFFNESS") as Float

            val pow = value.toDouble().pow(2.0)

            it.thisObject.putObject("mCenterXStiffness", (x / pow).toFloat())
            it.thisObject.putObject("mCenterYStiffness", (y / pow).toFloat())
            it.thisObject.putObject("mWidthStiffness", (width / pow).toFloat())
            it.thisObject.putObject("mRatioStiffness", (ratio / pow).toFloat())
            it.thisObject.putObject("mRadiusStiffness", (radius / pow).toFloat())
            it.thisObject.putObject("mAlphaStiffness", (alpha / pow).toFloat())

        }
    }

}

