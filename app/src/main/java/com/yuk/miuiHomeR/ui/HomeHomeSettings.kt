package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment

class HomeHomeSettings : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return HomeFragment()
    }

    class HomeFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_home_home
        }

        override fun initPrefs() {}
    }
}