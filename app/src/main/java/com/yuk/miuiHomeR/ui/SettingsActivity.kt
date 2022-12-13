package com.yuk.miuiHomeR.ui

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.BackupUtils
import com.yuk.miuiHomeR.utils.BackupUtils.backup
import com.yuk.miuiHomeR.utils.BackupUtils.handleCreateDocument
import com.yuk.miuiHomeR.utils.BackupUtils.handleReadDocument
import com.yuk.miuiHomeR.utils.BackupUtils.recovery
import com.yuk.miuiHomeR.utils.Locales
import com.yuk.miuiHomeR.utils.PrefsUtils
import moralnorm.preference.DropDownPreference
import moralnorm.preference.Preference
import moralnorm.preference.SwitchPreference
import java.util.Locale

class SettingsActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.settings)
        return SettingsFragment()
    }

    class SettingsFragment : SubFragment(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        var mHideIcon: SwitchPreference? = null
        var mLocaleSelector: DropDownPreference? = null
        var mBackupSettings: Preference? = null
        var mRestoreSettings: Preference? = null
        override fun getContentResId(): Int {
            return R.xml.prefs_settings
        }

        override fun initPrefs() {
            val mLocaleName = ArrayList<String>()
            mHideIcon = findPreference("prefs_key_settings_hide_icon")
            mLocaleSelector = findPreference("prefs_key_settings_language")
            mBackupSettings = findPreference("prefs_key_settings_backup")
            mRestoreSettings = findPreference("prefs_key_settings_restore")
            val displayLocaleTags = Locales.DISPLAY_LOCALES
            for (displayLocale in displayLocaleTags) {
                if (displayLocale == "SYSTEM") {
                    mLocaleName.add(0, getString(R.string.system_default))
                    continue
                }
                val localizedLocale = Locale.forLanguageTag(displayLocale)
                mLocaleName.add(localizedLocale.getDisplayName(localizedLocale))
            }
            mLocaleSelector?.setEntries(mLocaleName.toTypedArray<CharSequence>())
            mHideIcon?.onPreferenceChangeListener = this
            mLocaleSelector?.onPreferenceChangeListener = this
            mBackupSettings?.onPreferenceClickListener = this
            mRestoreSettings?.onPreferenceClickListener = this
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (preference === mBackupSettings) {
                PrefsUtils.mSharedPreferences?.let { backup(requireActivity(), it) }
            } else if (preference === mRestoreSettings) {
                PrefsUtils.mSharedPreferences?.let { recovery(requireActivity(), it) }
            }
            return true
        }

        override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
            if (preference === mHideIcon) {
                val pm = requireActivity().packageManager
                val mComponentEnabledState: Int = if (o as Boolean) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                }
                pm.setComponentEnabledSetting(
                    ComponentName(requireActivity(), MainActivity::class.java.name + "Alias"),
                    mComponentEnabledState,
                    PackageManager.DONT_KILL_APP
                )
            } else if (preference === mLocaleSelector) {
                requireActivity().recreate()
            }
            return true
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            BackupUtils.CREATE_DOCUMENT_CODE -> handleCreateDocument(this, data.data)
            BackupUtils.OPEN_DOCUMENT_CODE -> handleReadDocument(this, data.data)
        }
    }
}