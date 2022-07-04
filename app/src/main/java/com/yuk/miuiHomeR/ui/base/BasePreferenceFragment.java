package com.yuk.miuiHomeR.ui.base;

import android.content.Context;
import android.os.Bundle;

import com.yuk.miuiHomeR.utils.PrefsUtils;

import java.util.Objects;

import moralnorm.preference.PreferenceFragmentCompat;
import moralnorm.preference.PreferenceManager;

public class BasePreferenceFragment extends PreferenceFragmentCompat {


    public void onCreate(Bundle savedInstanceState, int prefs_default) {
        super.onCreate(savedInstanceState);
        try {
            getPreferenceManager().setSharedPreferencesName(PrefsUtils.mPrefsName);
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            getPreferenceManager().setStorageDeviceProtected();
            PreferenceManager.setDefaultValues(requireActivity(), prefs_default, false);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}
