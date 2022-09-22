package com.yuk.miuiHomeR.hook

import android.content.ComponentName
import android.content.Intent
import android.view.View
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.MainActivity
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectField
import com.yuk.miuiHomeR.utils.ktx.hookAfterAllMethods
import com.yuk.miuiHomeR.utils.ktx.setObjectField
import de.robv.android.xposed.XposedHelpers


object HomeSettings : BaseHook() {

    override fun init() {
        "com.miui.home.settings.MiuiHomeSettings".findClass().hookAfterAllMethods("onCreatePreferences") {
            val mLayoutResId = (it.thisObject.getObjectField("mAllAppsSetting"))?.getObjectField("mLayoutResId")
            val mWidgetLayoutResId = (it.thisObject.getObjectField("mAllAppsSetting"))?.getObjectField("mWidgetLayoutResId")
            val pref = XposedHelpers.newInstance("com.miui.home.settings.preference.ValuePreference".findClass(), appContext).apply {
                setObjectField("mTitle", "MiuiHome")
                setObjectField("mOrder", 0)
                setObjectField("mVisible", true)
                setObjectField("mLayoutResId", mLayoutResId)
                setObjectField("mWidgetLayoutResId", mWidgetLayoutResId)
                setObjectField("mFragment", "MiuiHomeR")
                callMethod("setValue", moduleRes.getString(R.string.module_settings))
                setObjectField("mClickListener", object : View.OnClickListener {
                    override fun onClick(v: View) {
                        val intent = Intent()
                        intent.component = ComponentName("com.yuk.miuiHomeR", "com.yuk.miuiHomeR.ui.MainActivity")
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        v.context.startActivity(intent)
                    }
                })
                callMethod("setIntent", Intent())
            }
            it.thisObject.callMethod("getPreferenceScreen")?.callMethod("addPreference", pref)

        }
    }
}