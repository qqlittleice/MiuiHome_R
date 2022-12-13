package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment

class OtherActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.other_features)
        return OtherFeaturesFragment()
    }

    class OtherFeaturesFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_other
        }

        override fun initPrefs() {}
    }
}