package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment

class AppDrawerActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return AppDrawerFragment()
    }

    class AppDrawerFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_app_drawer
        }

        override fun initPrefs() {

        }
    }
}