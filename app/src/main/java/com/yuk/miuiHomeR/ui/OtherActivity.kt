package com.yuk.miuiHomeR.ui

import android.content.ComponentName
import android.content.Intent
import android.text.TextUtils
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.Locales
import com.yuk.miuiHomeR.utils.ktx.setLocale
import moralnorm.preference.ListPreference
import moralnorm.preference.Preference
import java.util.Locale

class OtherActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return OtherFeaturesFragment()
    }

    class OtherFeaturesFragment : SubFragment() {
        private lateinit var languagePreference: ListPreference
        override fun getContentResId(): Int {
            return R.xml.prefs_other
        }

        override fun initPrefs() {
            languagePreference = findPreference("prefs_key_language")
            languagePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->


                if (newValue is String) {
                    val appLocale: Locale = if ("SYSTEM" == newValue) {
                        Locale(resources.configuration.locale.toLanguageTag())
                    } else {
                        Locale.forLanguageTag(newValue)
                    }
                    setLocale(resources, appLocale)
                    val intent = Intent()
                    intent.component = ComponentName("com.yuk.miuiHomeR", "com.yuk.miuiHomeR.ui.MainActivity")
                    activity?.finishAffinity()
                    activity?.startActivity(intent)
                }
                true
            }
            setupLocalePreference()

        }

        private fun setupLocalePreference() {
            val localeTags = Locales.LOCALES
            val displayLocaleTags = Locales.DISPLAY_LOCALES
            languagePreference.entries = displayLocaleTags
            languagePreference.entryValues = localeTags
            val currentLocaleTag = languagePreference.value
            val currentLocaleIndex = localeTags.indexOf(currentLocaleTag)
            val currentLocale = Locale(currentLocaleTag)
            val localizedLocales = mutableListOf<CharSequence>()
            for ((index, displayLocale) in displayLocaleTags.withIndex()) {
                if (index == 0) {
                    localizedLocales.add(getString(R.string.follow_system))
                    continue
                }
                val locale = Locale.forLanguageTag(displayLocale.toString())
                val localeName = if (!TextUtils.isEmpty(locale.script)) locale.getDisplayScript(locale)
                else locale.getDisplayName(locale)
                val localizedLocaleName = if (!TextUtils.isEmpty(locale.script)) locale.getDisplayScript(currentLocale)
                else locale.getDisplayName(currentLocale)
                localizedLocales.add(
                    if (index != currentLocaleIndex) {
                        HtmlCompat.fromHtml(
                            "$localeName<br><small>$localizedLocaleName<small>", HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        localizedLocaleName
                    }
                )
            }
            languagePreference.entries = localizedLocales.toTypedArray()
            languagePreference.summary = when {
                TextUtils.isEmpty(currentLocaleTag) || "SYSTEM" == currentLocaleTag -> {
                    getString(R.string.follow_system)
                }

                currentLocaleIndex != -1 -> {
                    val localizedLocale = localizedLocales[currentLocaleIndex]
                    val newLineIndex = localizedLocale.indexOf('\n')
                    if (newLineIndex == -1) {
                        localizedLocale.toString()
                    } else {
                        localizedLocale.subSequence(0, newLineIndex).toString()
                    }
                }

                else -> {
                    ""
                }
            }
        }
    }
}