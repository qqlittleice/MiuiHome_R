package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment

class WidgetActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.widget)
        return WidgetFragment()
    }

    class WidgetFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_widget
        }

        override fun initPrefs() {}
    }
}