package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment

class RecentActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return RecentFragment()
    }

    class RecentFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_recent
        }

        override fun initPrefs() {}
    }
}