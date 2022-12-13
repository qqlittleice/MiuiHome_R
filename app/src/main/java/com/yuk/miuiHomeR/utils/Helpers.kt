package com.yuk.miuiHomeR.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.AsyncTask
import com.yuk.miuiHomeR.BuildConfig
import java.io.File

object Helpers {
    const val mAppModulePkg = BuildConfig.APPLICATION_ID

    @Synchronized
    fun getProtectedContext(context: Context): Context {
        return getProtectedContext(context, null)
    }

    @Synchronized
    fun getProtectedContext(context: Context, config: Configuration?): Context {
        return try {
            val mContext = if (context.isDeviceProtectedStorage) context else context.createDeviceProtectedStorageContext()
            if (config == null) mContext else mContext.createConfigurationContext(config)
        } catch (t: Throwable) {
            context
        }
    }

    @SuppressLint("SetWorldReadable", "SetWorldWritable")
    fun fixPermissionsAsync(context: Context) {
        AsyncTask.execute {
            try {
                Thread.sleep(500)
            } catch (ignore: Throwable) {
            }
            val pkgFolder = context.dataDir
            if (pkgFolder.exists()) {
                pkgFolder.setExecutable(true, false)
                pkgFolder.setReadable(true, false)
                pkgFolder.setWritable(true, false)
            }
            val sharedPrefsFolder = File(PrefsUtils.sharedPrefsPath!!)
            if (sharedPrefsFolder.exists()) {
                sharedPrefsFolder.setExecutable(true, false)
                sharedPrefsFolder.setReadable(true, false)
                sharedPrefsFolder.setWritable(true, false)
            }
            val sharedPrefsFile = File(PrefsUtils.sharedPrefsFile!!)
            if (sharedPrefsFile.exists()) {
                sharedPrefsFile.setReadable(true, false)
                sharedPrefsFile.setExecutable(true, false)
                sharedPrefsFile.setWritable(true, false)
            }
        }
    }
}