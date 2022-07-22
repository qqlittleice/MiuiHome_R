package com.yuk.miuiHomeR.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.DeviceUtils
import moralnorm.appcompat.app.AppCompatActivity
import moralnorm.preference.Preference

class AboutActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return AboutFragment()
    }

    class AboutFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_about
        }

        override fun initPrefs() {

        }
    }
}