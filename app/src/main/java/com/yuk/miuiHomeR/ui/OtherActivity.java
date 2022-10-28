package com.yuk.miuiHomeR.ui;


import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.service.KillSelfService;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.Locales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import moralnorm.preference.DropDownPreference;

public class OtherActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new OtherFeaturesFragment();
    }

    public static class OtherFeaturesFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_other;
        }

        @Override
        public void initPrefs() {
            String[] locales = Locales.LOCALES;
            ArrayList<String> localesArr = new ArrayList<>(Arrays.asList(locales));
            ArrayList<String> localeNames = new ArrayList<>();
            for (String locale: localesArr) try {
                Locale loc = Locale.forLanguageTag(locale);
                StringBuilder locStr;
                if (locale.equals("SYSTEM")) {
                    locStr = new StringBuilder(getString(R.string.follow_system));
                } else {
                    locStr = new StringBuilder(loc.getDisplayName(loc));
                }
                localeNames.add(locStr.toString());
            } catch (Throwable t) {
                localeNames.add(Locale.getDefault().getDisplayLanguage(Locale.getDefault()));
            }

            DropDownPreference locale = findPreference("prefs_key_language");
            locale.setEntries(localeNames.toArray(new CharSequence[0]));
            locale.setEntryValues(localesArr.toArray(new CharSequence[0]));
            locale.setOnPreferenceChangeListener((preference, o) -> {
                restartApp(getContext(), (String) o, 10L);
                return true;
            });
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
}
