package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.ktx.checkMiuiVersion
import moralnorm.preference.Preference

class RecentActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.recent)
        return RecentFragment()
    }

    class RecentFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_recent
        }

        override fun initPrefs() {

            val mSmallWindowVisible = findPreference<Preference>("prefs_key_home_hide_small_window")
            mSmallWindowVisible.isVisible = checkMiuiVersion() < 14f
            mSmallWindowVisible.isEnabled = mSmallWindowVisible.isVisible
        }
    }
}