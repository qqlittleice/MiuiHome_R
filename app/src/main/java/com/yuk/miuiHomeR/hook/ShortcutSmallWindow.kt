package com.yuk.miuiHomeR.hook

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.callStaticMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getStaticObjectField
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllMethods
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import com.yuk.miuiHomeR.utils.ktx.setStaticObjectField
import de.robv.android.xposed.XposedHelpers

@SuppressLint("StaticFieldLeak", "DiscouragedApi")
object ShortcutSmallWindow : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("shortcut_small_window")) return
        val mViewDarkModeHelper = ("com.miui.home.launcher.util.ViewDarkModeHelper").findClass()
        val mSystemShortcutMenu = ("com.miui.home.launcher.shortcuts.SystemShortcutMenu").findClass()
        val mSystemShortcutMenuItem = ("com.miui.home.launcher.shortcuts.SystemShortcutMenuItem").findClass()
        val mAppShortcutMenu = ("com.miui.home.launcher.shortcuts.AppShortcutMenu").findClass()
        val mShortcutMenuItem = ("com.miui.home.launcher.shortcuts.ShortcutMenuItem").findClass()
        val mAppDetailsShortcutMenuItem = ("com.miui.home.launcher.shortcuts.SystemShortcutMenuItem\$AppDetailsShortcutMenuItem").findClass()
        val mActivityUtilsCompat = ("com.miui.launcher.utils.ActivityUtilsCompat").findClass()
        mViewDarkModeHelper.hookAfterAllMethods("onConfigurationChanged") {
            mSystemShortcutMenuItem.callStaticMethod("createAllSystemShortcutMenuItems")
        }
        mShortcutMenuItem.hookAfterAllMethods("getShortTitle") {
            if (it.result == "应用信息") {
                it.result = "信息"
            }
        }
        mAppDetailsShortcutMenuItem.hookBeforeMethod("lambda\$getOnClickListener$0", mAppDetailsShortcutMenuItem, View::class.java) {
            val obj = it.args[0]
            val view: View = it.args[1] as View
            val mShortTitle = obj.callMethod("getShortTitle") as CharSequence
            if (mShortTitle == moduleRes.getString(R.string.small_window)) {
                it.result = null
                val intent = Intent()
                val mComponentName = obj.callMethod("getComponentName") as ComponentName
                intent.action = "android.intent.action.MAIN"
                intent.addCategory("android.intent.category.DEFAULT")
                intent.component = mComponentName
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val callStaticMethod = mActivityUtilsCompat.callStaticMethod("makeFreeformActivityOptions", view.context, mComponentName.packageName)
                if (callStaticMethod != null) {
                    view.context.startActivity(intent, callStaticMethod.callMethod("toBundle") as Bundle)
                }
            }
        }
        mSystemShortcutMenu.hookAfterAllMethods("getMaxShortcutItemCount") {
            it.result = 5
        }
        mAppShortcutMenu.hookAfterAllMethods("getMaxShortcutItemCount") {
            it.result = 5
        }
        mSystemShortcutMenuItem.hookAfterAllMethods("createAllSystemShortcutMenuItems") {
            val mAllSystemShortcutMenuItems = mSystemShortcutMenuItem.getStaticObjectField("sAllSystemShortcutMenuItems") as Collection<Any>
            val mSmallWindowInstance = XposedHelpers.newInstance(mAppDetailsShortcutMenuItem)
            mSmallWindowInstance.callMethod("setShortTitle", moduleRes.getString(R.string.small_window))
            mSmallWindowInstance.callMethod(
                "setIconDrawable", ContextCompat.getDrawable(
                    appContext, appContext.resources.getIdentifier("ic_task_small_window", "drawable", "com.miui.home")
                )
            )
            val sAllSystemShortcutMenuItems = ArrayList<Any>()
            sAllSystemShortcutMenuItems.add(mSmallWindowInstance)
            sAllSystemShortcutMenuItems.addAll(mAllSystemShortcutMenuItems)
            mSystemShortcutMenuItem.setStaticObjectField("sAllSystemShortcutMenuItems", sAllSystemShortcutMenuItems)
        }
    }

}
