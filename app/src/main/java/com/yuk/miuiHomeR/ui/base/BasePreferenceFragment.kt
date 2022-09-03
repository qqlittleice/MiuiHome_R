package com.yuk.miuiHomeR.ui.base

import android.content.Context
import android.os.Bundle
import com.yuk.miuiHomeR.utils.PrefsUtils
import moralnorm.preference.PreferenceFragmentCompat
import moralnorm.preference.PreferenceManager

open class BasePreferenceFragment : PreferenceFragmentCompat() {

    fun onCreate(savedInstanceState: Bundle?, prefs_default: Int) {
        super.onCreate(savedInstanceState)
        try {
            preferenceManager.sharedPreferencesName = PrefsUtils.mPrefsName
            preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
            preferenceManager.setStorageDeviceProtected()
            PreferenceManager.setDefaultValues(requireActivity(), prefs_default, false)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {}
}