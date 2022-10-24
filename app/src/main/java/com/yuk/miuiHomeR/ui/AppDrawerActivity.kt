package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.ktx.isLegacyAndroid
import moralnorm.preference.Preference

class AppDrawerActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return AppDrawerFragment()
    }

    class AppDrawerFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_app_drawer
        }

        override fun initPrefs() {
            val mAllAppsBlurVisible = findPreference<Preference>("prefs_key_home_all_apps_blur")
            mAllAppsBlurVisible.isVisible = !isLegacyAndroid()
            mAllAppsBlurVisible.isEnabled = mAllAppsBlurVisible.isVisible
        }
    }
}