package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class FolderActivity extends BaseAppCompatActivity {
    @Override
    public Fragment initFragment() {
        return new FolderFragment();
    }

    public static class FolderFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_folder;
        }

        @Override
        public void initPrefs() {

        }
    }
}
