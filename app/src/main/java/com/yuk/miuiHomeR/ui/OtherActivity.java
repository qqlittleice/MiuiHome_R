package com.yuk.miuiHomeR.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class OtherActivity extends BaseAppCompatActivity {

    @NonNull
    @Override
    public Fragment initFragment() {
        setTitle(R.string.other_features);
        return new OtherFeaturesFragment();
    }

    public static class OtherFeaturesFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_other;
        }

        @Override
        public void initPrefs() {
        }
    }
}
