package com.yuk.miuiHomeR

import android.content.Context
import com.yuk.miuiHomeR.utils.PrefsUtils
import com.yuk.miuiHomeR.utils.PrefsUtils.getSharedPrefs
import com.yuk.miuiHomeR.utils.ktx.setLocale
import java.util.Locale

class Application : android.app.Application() {

    override fun attachBaseContext(base: Context?) {
        PrefsUtils.mSharedPreferences = base?.let { getSharedPrefs(it, false) }
        val locale: String? = PrefsUtils.mSharedPreferences?.getString("prefs_key_settings_language", "SYSTEM")
        if (locale != null) {
            if (base != null)
                super.attachBaseContext(setLocale(base, Locale.forLanguageTag(locale)))
        } else super.attachBaseContext(base)
    }
}