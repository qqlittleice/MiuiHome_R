package com.yuk.miuiHomeR.ui

import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.ktx.atLeastAndroidS
import moralnorm.preference.Preference

class FolderActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return FolderFragment()
    }

    class FolderFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_folder
        }

        override fun initPrefs() {
            val mSmallFolderBlurVisible = findPreference<Preference>("prefs_key_small_folder_blur")
            mSmallFolderBlurVisible.isVisible = atLeastAndroidS()
            mSmallFolderBlurVisible.isEnabled = mSmallFolderBlurVisible.isVisible
        }
    }
}