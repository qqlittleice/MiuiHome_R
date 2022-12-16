package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.ktx.atLeastAndroidS
import moralnorm.preference.Preference

class HomeActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.home)
        return HomeFragment()
    }

    class HomeFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_home
        }

        override fun initPrefs() {
            val mShortcutBlurVisible = findPreference<Preference>("prefs_key_home_shortcut_blur")
            mShortcutBlurVisible.isVisible = atLeastAndroidS()
            mShortcutBlurVisible.isEnabled = mShortcutBlurVisible.isVisible
        }
    }
}