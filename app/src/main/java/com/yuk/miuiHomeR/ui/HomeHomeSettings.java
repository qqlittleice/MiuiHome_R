package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.DeviceUtils;

import moralnorm.preference.SwitchPreference;

public class HomeHomeSettings extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new HomeFragment();
    }

    public static class HomeFragment extends SubFragment {

        @Override
        public int getContentResId() { return R.xml.prefs_home_home; }

        @Override
        public void initPrefs() {
            SwitchPreference mPadDockBlur = findPreference("prefs_key_pad_dock_blur");
            mPadDockBlur.setVisible(DeviceUtils.isPadDevice());
        }
    }
}
