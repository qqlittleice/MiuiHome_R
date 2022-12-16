package com.yuk.miuiHomeR.hook

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectField
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllConstructors
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.yuk.miuiHomeR.utils.ktx.setIntField

object Recent : BaseHook() {
    override fun init() {
        val recentsContainerClass = "com.miui.home.recents.views.RecentsContainer".findClass()

        if (mPrefsMap.getBoolean("home_hide_clean_up")) {
            recentsContainerClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mView = it.thisObject.getObjectField("mClearAnimView") as View
                mView.visibility = View.GONE
            }
        }

        val appCardBgColor = mPrefsMap.getInt("recents_card_bg_color", -1)
        if (appCardBgColor != -1) {
            "com.miui.home.recents.views.TaskViewThumbnail".findClass().hookAfterAllConstructors {
                it.thisObject.setIntField("mBgColorForSmallWindow", appCardBgColor)
            }
        }

        val recentTextSize = mPrefsMap.getInt("recents_text_size", -1)
        if (recentTextSize != -1) {
            val taskViewHeaderClass = "com.miui.home.recents.views.TaskViewHeader".findClass()
            taskViewHeaderClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleView") as TextView
                mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, recentTextSize.toFloat())
                if (recentTextSize == 0) mTitle.visibility = View.GONE
            }
        }

        val recentTextColor = mPrefsMap.getInt("recents_text_color", -1)
        if (recentTextSize != -1) {
            val taskViewHeaderClass = "com.miui.home.recents.views.TaskViewHeader".findClass()
            taskViewHeaderClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleView") as TextView
                mTitle.setTextColor(recentTextColor)
            }
        }

        val emptyViewText = mPrefsMap.getString("recent_text", "")
        if (emptyViewText != "") {
            "com.miui.home.recents.views.RecentsView".hookAfterMethod(
                "showEmptyView", Int::class.javaPrimitiveType
            ) {
                (it.thisObject.getObjectField("mEmptyView") as TextView).apply {
                    this.text = emptyViewText
                }
            }
        }

        if (mPrefsMap.getBoolean("recents_icon")) {
            "com.miui.home.recents.views.TaskViewHeader".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mImage = it.thisObject.getObjectField("mIconView") as ImageView
                mImage.visibility = View.GONE
            }
        }

    }
}