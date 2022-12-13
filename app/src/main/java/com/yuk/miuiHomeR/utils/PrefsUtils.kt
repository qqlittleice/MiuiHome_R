package com.yuk.miuiHomeR.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.yuk.miuiHomeR.utils.Helpers.getProtectedContext
import java.io.File

object PrefsUtils {
    const val mPrefsName = "MiuiHome_Config"
    private var mPrefsPathCurrent: String? = null
    private var mPrefsFileCurrent: String? = null
    var mSharedPreferences: SharedPreferences? = null
    private const val mPrefsPath = "/data/user_de/0/" + Helpers.mAppModulePkg + "/shared_prefs"
    private const val mPrefsFile = "$mPrefsPath/$mPrefsName.xml"

    @SuppressLint("WorldReadableFiles")
    fun getSharedPrefs(context: Context, protectedStorage: Boolean, multiProcess: Boolean): SharedPreferences {
        var c = context
        if (protectedStorage) c = getProtectedContext(c)
        return try {
            c.getSharedPreferences(
                mPrefsName, if (multiProcess) Context.MODE_MULTI_PROCESS or Context.MODE_WORLD_READABLE else Context.MODE_WORLD_READABLE
            )
        } catch (t: Throwable) {
            c.getSharedPreferences(mPrefsName, if (multiProcess) Context.MODE_MULTI_PROCESS or Context.MODE_PRIVATE else Context.MODE_PRIVATE)
        }
    }

    fun getSharedPrefs(context: Context, protectedStorage: Boolean): SharedPreferences {
        return getSharedPrefs(context, protectedStorage, false)
    }

    val sharedPrefsPath: String?
        get() = if (mPrefsPathCurrent == null) try {
            val mFile = mSharedPreferences!!.javaClass.getDeclaredField("mFile")
            mFile.isAccessible = true
            mPrefsPathCurrent = (mFile[mSharedPreferences] as File).parentFile?.absolutePath
            mPrefsPathCurrent
        } catch (t: Throwable) {
            print("Test$t")
            mPrefsPath
        } else mPrefsPathCurrent
    val sharedPrefsFile: String?
        get() {
            if (mPrefsFileCurrent == null) return try {
                val fFile = mSharedPreferences!!.javaClass.getDeclaredField("mFile")
                fFile.isAccessible = true
                mPrefsFileCurrent = (fFile[mSharedPreferences] as File).absolutePath
                println("Test: mPrefsFileCurrent")
                mPrefsFileCurrent
            } catch (t: Throwable) {
                println("Test: mPrefsFile$t")
                mPrefsFile
            } else println("Test: mPrefsFileCurrent2")
            return mPrefsFileCurrent
        }
}