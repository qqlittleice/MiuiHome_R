package com.yuk.miuiHomeR

import android.content.Context
import android.os.Process
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuiHomeR.hook.*
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.PrefsMap
import com.yuk.miuiHomeR.utils.PrefsUtils
import com.yuk.miuiHomeR.utils.ktx.getProp
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val PACKAGE_NAME_HOOKED = "com.miui.home"
private const val TAG = "MiuiHomeR"
var mPrefsMap = PrefsMap<String, Any>()
var versionName: String? = null
var isAlpha: Boolean? = null
var versionCode: Long? = null
var miuiVersion: String? = null
var androidVersion: String? = null
var isPadDevice: Boolean? = null

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit /* Optional */ {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            PACKAGE_NAME_HOOKED -> {
                EzXHelperInit.initHandleLoadPackage(lpparam)
                EzXHelperInit.setLogTag(TAG)
                EzXHelperInit.setToastTag(TAG)
                EzXHelperInit.setLogXp(true)
                "com.miui.home.launcher.Application".hookAfterMethod(
                    "attachBaseContext", Context::class.java
                ) {
                    EzXHelperInit.initAppContext(it.args[0] as Context)
                    versionName = checkVersionName()
                    isAlpha = checkIsAlpha()
                    versionCode = checkVersionCode()
                    miuiVersion = checkMiuiVersion()
                    androidVersion = checkAndroidVersion()
                    isPadDevice = checkIsPadDevice()
                }
                initHooks(
                    AllowMoveAllWidgetToMinus,
                    AlwaysBlurWallpaper,
                    AlwaysShowMiuiWidget,
                    AlwaysShowStatusClock,
                    DisableRecentViewWallpaperDarken,
                    HideStatusBarWhenEnterRecent,
                    AnimDurationRatio,
                    DoubleTapToSleep,
                    HideWidgetTitles,
                    BlurLevel,
                    BlurRadius,
                    FolderColumnsCount,
                    EnableBlurWhenOpenFolder,
                    EnableFolderIconBlur
                )
            }
            else -> return
        }
    }

    // Optional
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)

        if (mPrefsMap.size == 0) {
            var mXSharedPreferences: XSharedPreferences? = null
            try {
                mXSharedPreferences =
                    XSharedPreferences(Helpers.mAppModulePkg, PrefsUtils.mPrefsName)
                mXSharedPreferences.makeWorldReadable()
            } catch (t: Throwable) {
                XposedBridge.log(t)
            }
            val allPrefs = mXSharedPreferences?.all
            if (allPrefs == null || allPrefs.isEmpty()) {
                XposedBridge.log("[UID " + Process.myUid() + "] Cannot read module's SharedPreferences, some mods might not work!")
            } else {
                mPrefsMap.putAll(allPrefs)
            }
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }

    fun checkVersionName(): String {
        return InitFields.appContext.packageManager.getPackageInfo(
            InitFields.appContext.packageName, 0
        ).versionName
    }

    fun checkIsAlpha(): Boolean {
        return (checkVersionName().contains("ALPHA", ignoreCase = true))
    }

    fun checkIsPadDevice(): Boolean {
        return XposedHelpers.callStaticMethod(
            loadClass("com.miui.home.launcher.common.Utilities"), "isPadDevice"
        ) as Boolean
    }

    fun checkMiuiVersion(): String {
        return when (getProp("ro.miui.ui.version.name")) {
            "V130" -> "13"
            "V125" -> "12.5"
            "V12" -> "12"
            "V11" -> "11"
            "V10" -> "10"
            else -> "?"
        }
    }

    fun checkAndroidVersion(): String {
        return getProp("ro.build.version.release")
    }

    fun checkVersionCode(): Long {
        return InitFields.appContext.packageManager.getPackageInfo(
            InitFields.appContext.packageName, 0
        ).longVersionCode
    }
}
