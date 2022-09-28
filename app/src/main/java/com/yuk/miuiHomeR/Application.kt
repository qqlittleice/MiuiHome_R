package com.yuk.miuiHomeR

import android.content.Context
import com.yuk.miuiHomeR.utils.PrefsUtils
import com.yuk.miuiHomeR.utils.PrefsUtils.getSharedPrefs
import com.yuk.miuiHomeR.utils.ktx.getLocale
import com.yuk.miuiHomeR.utils.ktx.setLocale

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        setLocale(resources, getLocale(applicationContext))
    }

    override fun attachBaseContext(base: Context?) {
        PrefsUtils.mSharedPreferences = getSharedPrefs(base, false)
        super.attachBaseContext(base)
    }
}