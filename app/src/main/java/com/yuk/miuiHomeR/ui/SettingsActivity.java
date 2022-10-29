package com.yuk.miuiHomeR.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.SpannableString;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.service.KillSelfService;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.BackupUtils;
import com.yuk.miuiHomeR.utils.PrefsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import moralnorm.preference.DropDownPreference;
import moralnorm.preference.Preference;
import moralnorm.preference.SwitchPreference;

public class SettingsActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        setTitle(R.string.settings);
        return new SettingsFragment();
    }

    public static class SettingsFragment extends SubFragment implements Preference.OnPreferenceClickListener,
            Preference.OnPreferenceChangeListener {

        SwitchPreference mHideIcon;
        DropDownPreference mLocaleSelector;
        Preference mBackupSettings;
        Preference mRestoreSettings;

        String[] locales = new String[] {"zh-CN", "zh-TW", "zh-HK", "af", "ar", "ca", "cs", "da", "de", "el", "en", "es", "fa", "fi", "fr", "he", "hu", "id", "it", "ja", "ka", "ko", "nl", "no", "pl", "pt", "pt-BR", "ro", "ru", "ru-RU", "sr", "sv", "th", "tr", "uk", "uz", "vi"};
        ArrayList<String> mLocale = new ArrayList<>(Arrays.asList(locales));
        ArrayList<String> mLocaleName = new ArrayList<>();

        @Override
        public int getContentResId() {
            return R.xml.prefs_settings;
        }

        @Override
        public void initPrefs() {
            getLocales();
            mHideIcon = findPreference("prefs_key_settings_hide_icon");
            mLocaleSelector = findPreference("prefs_key_settings_language");
            mBackupSettings = findPreference("prefs_key_settings_backup");
            mRestoreSettings = findPreference("prefs_key_settings_restore");

            mLocaleSelector.setEntries(mLocaleName.toArray(new CharSequence[0]));
            mLocaleSelector.setEntryValues(mLocale.toArray(new CharSequence[0]));

            mHideIcon.setOnPreferenceChangeListener(this);
            mLocaleSelector.setOnPreferenceChangeListener(this);
            mBackupSettings.setOnPreferenceClickListener(this);
            mRestoreSettings.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mBackupSettings) {
                BackupUtils.INSTANCE.backup(getActivity(), PrefsUtils.mSharedPreferences);
            } else if (preference == mRestoreSettings) {
                BackupUtils.INSTANCE.recovery(getActivity(), PrefsUtils.mSharedPreferences);
            }
            return true;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            if (preference == mHideIcon) {
                PackageManager pm = getActivity().getPackageManager();
                int mComponentEnabledState;
                if ((Boolean)o) {
                    mComponentEnabledState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                } else {
                    mComponentEnabledState =  PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                }
                pm.setComponentEnabledSetting(new ComponentName(getActivity(), MainActivity.class.getName() + "Alias"), mComponentEnabledState, PackageManager.DONT_KILL_APP);
            } else if (preference == mLocaleSelector) {
                restartApp(getContext(), (String) o, 10L);
            }
            return true;
        }

        public void getLocales() {
            for (String locale : mLocale) try {
                Locale loc = Locale.forLanguageTag(locale);
                String locStr;
                /*if (locale.equals("zh-CN")) {
                    locStr = "简体中文_中国";
                } else if (locale.equals("zh-TW")) {
                    locStr = "繁体中文_中国台湾";
                } else {
                    locStr = loc.getDisplayName(loc);
                }*/
                locStr = loc.getDisplayName(loc);
                mLocaleName.add(locStr);
            } catch (Throwable t) {
                mLocaleName.add(Locale.getDefault().getDisplayLanguage(Locale.getDefault()));
            }

            mLocale.add(0, "SYSTEM");
            mLocaleName.add(0, getString(R.string.system_default));
        }

        /**
         * 重启整个APP
         * @param context
         * @param delayed 延迟多少毫秒
         */
        private void restartApp(Context context, String locale, Long delayed) {
            //开启一个新的服务，用来重启本APP
            Intent intent = new  Intent(context, KillSelfService.class);
            intent.putExtra("PackageName", context.getPackageName());
            intent.putExtra("Locale", locale);
            intent.putExtra("Delayed", delayed);
            context.startService(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case BackupUtils.CREATE_DOCUMENT_CODE :
                BackupUtils.INSTANCE.handleCreateDocument(this, data.getData());
                break;

            case BackupUtils.OPEN_DOCUMENT_CODE :
                BackupUtils.INSTANCE.handleReadDocument(this, data.getData());
                break;
        }
    }

}
