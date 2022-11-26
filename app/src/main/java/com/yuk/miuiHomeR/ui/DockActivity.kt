package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.ktx.atLeastAndroidS
import com.yuk.miuiHomeR.utils.ktx.isPadDevice
import moralnorm.preference.Preference

class DockActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.dock_settings)
        return DockFragment()
    }

    class DockFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_dock
        }

        override fun initPrefs() {
            val mDockVisible = findPreference<Preference>("prefs_key_home_dock_blur")
            mDockVisible.isVisible = !isPadDevice() && atLeastAndroidS()
            mDockVisible.isEnabled = mDockVisible.isVisible
            val mDockTitleVisible = findPreference<Preference>("prefs_key_home_dock_icon_title")
            mDockTitleVisible.isVisible = !isPadDevice()
            mDockTitleVisible.isEnabled = mDockTitleVisible.isVisible
        }
    }
}
