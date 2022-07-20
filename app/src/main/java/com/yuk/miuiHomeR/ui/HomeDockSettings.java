package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class HomeDockSettings extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new HomeDockSettingsFragment();
    }

    public static class HomeDockSettingsFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_home_dock;
        }

        @Override
        public void initPrefs() {

        }
    }
}
