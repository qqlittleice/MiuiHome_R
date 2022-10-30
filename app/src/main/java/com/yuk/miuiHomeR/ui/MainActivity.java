package com.yuk.miuiHomeR.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.provider.SharedPrefsProvider;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.Helpers;
import com.yuk.miuiHomeR.utils.PrefsUtils;
import com.yuk.miuiHomeR.utils.ktx.AppUtilKt;

import java.io.File;
import java.util.Set;

import moralnorm.appcompat.app.ActionBar;
import moralnorm.preference.Preference;

public class MainActivity extends BaseAppCompatActivity {

    private FileObserver fileObserver;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

    private ImageView mActionBarMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppCompatActionBar().setDisplayHomeAsUpEnabled(getIntent().getBooleanExtra("homeAsUpEnabled", false));
        initView();
        initData();
    }

    private void initView() {
        mActionBarMoreView = new ImageView(this);
        mActionBarMoreView.setImageResource(R.drawable.ic_settings);
        ActionBar actionBar = getAppCompatActionBar();
        actionBar.setEndView(mActionBarMoreView);

        mActionBarMoreView.setOnClickListener(view -> startActivity(new Intent(this,
                SettingsActivity.class)));
    }

    private void initData() {
        mPreferenceChangeListener = (sharedPreferences, s) -> {
            Log.i("prefs", "Changed: " + s);
            Object val = sharedPreferences.getAll().get(s);
            String path = "";
            if (val instanceof String)
                path = "string/";
            else if (val instanceof Set<?>)
                path = "stringset/";
            else if (val instanceof Integer)
                path = "integer/";
            else if (val instanceof Boolean)
                path = "boolean/";
            getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null);
            if (!path.equals("")) getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s), null);
        };

        PrefsUtils.mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
        Helpers.fixPermissionsAsync(getApplicationContext());

        try {
            fileObserver = new FileObserver(new File(PrefsUtils.getSharedPrefsPath()), FileObserver.CLOSE_WRITE) {
                @Override
                public void onEvent(int event, String path) {
                    Helpers.fixPermissionsAsync(getApplicationContext());
                }
            };
            fileObserver.startWatching();
        } catch (Throwable t) {
            Log.e("prefs", "Failed to start FileObserver!");
        }
    }

    @NonNull
    @Override
    public Fragment initFragment() {
        return new MainFragment();
    }

    public static class MainFragment extends SubFragment implements Preference.OnPreferenceClickListener {

        Preference mRebootHome;

        @Override
        public int getContentResId() {
            return R.xml.prefs_main;
        }

        @Override
        public void initPrefs() {
            mRebootHome = findPreference("reboot_home");
            mRebootHome.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mRebootHome) {
                AppUtilKt.execShell("am force-stop com.miui.home && am force-stop com.yuk.miuiHomeR");
            }
            return false;
        }
    }
}
