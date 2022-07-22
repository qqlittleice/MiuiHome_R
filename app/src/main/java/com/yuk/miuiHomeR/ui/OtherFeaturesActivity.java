package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.DeviceUtils;

import moralnorm.preference.SwitchPreference;

public class OtherFeaturesActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new OtherFeaturesFragment();
    }

    public static class OtherFeaturesFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_other_features;
        }

        @Override
        public void initPrefs() {

        }
    }
}
