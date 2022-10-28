package com.yuk.miuiHomeR.ui.base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.utils.Helpers
import moralnorm.appcompat.app.AppCompatActivity
import moralnorm.internal.utils.ViewUtils

abstract class BaseAppCompatActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(Helpers.getLocaleContext(newBase))
    }

    override fun getResources(): Resources {
        return try {
            val context : Context = Helpers.getLocaleContext(applicationContext)
            context.getResources()
        } catch (t: Throwable) {
            t.printStackTrace()
            super.getResources()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (ViewUtils.isNightMode(this)) R.style.AppTheme_Dark else R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, initFragment())
            .commit()
    }

    abstract fun initFragment(): Fragment

    fun startActivity(activity: AppCompatActivity, cls: Class<*>) {
        startActivity(Intent(activity, cls))
    }
}