package com.yuk.miuiHomeR

import android.content.Context
import com.yuk.miuiHomeR.utils.PrefsUtils

class Application : android.app.Application() {

    override fun attachBaseContext(base: Context?) {
        PrefsUtils.mSharedPreferences = PrefsUtils.getSharedPrefs(base, false)
        super.attachBaseContext(base)
    }
}