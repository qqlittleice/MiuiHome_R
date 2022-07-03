package com.yuk.miuiHomeR;

import android.content.Context;

import com.yuk.miuiHomeR.utils.PrefsUtils;

public class Application extends android.app.Application {

    @Override
    protected void attachBaseContext(Context base) {
        PrefsUtils.mSharedPreferences = PrefsUtils.getSharedPrefs(base, false);
        super.attachBaseContext(base);
    }
}