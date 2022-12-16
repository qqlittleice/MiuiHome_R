package com.yuk.miuiHomeR

import android.app.Application
import android.content.Context
import android.os.Process
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import com.yuk.miuiHomeR.hook.AllAppsContainerViewBlur
import com.yuk.miuiHomeR.hook.AllowMoveAllWidgetToMinus
import com.yuk.miuiHomeR.hook.AlwaysBlurWallpaper
import com.yuk.miuiHomeR.hook.AlwaysShowMiuiWidget
import com.yuk.miuiHomeR.hook.AlwaysShowStatusClock
import com.yuk.miuiHomeR.hook.AnimDurationRatio
import com.yuk.miuiHomeR.hook.AppDrawer
import com.yuk.miuiHomeR.hook.BaseHook
import com.yuk.miuiHomeR.hook.BigIconCorner
import com.yuk.miuiHomeR.hook.BlurLevel
import com.yuk.miuiHomeR.hook.BlurRadius
import com.yuk.miuiHomeR.hook.CloseFolderWhenLaunchedApp
import com.yuk.miuiHomeR.hook.DisableRecentViewWallpaperDarken
import com.yuk.miuiHomeR.hook.Dock
import com.yuk.miuiHomeR.hook.DoubleTapToSleep
import com.yuk.miuiHomeR.hook.EnableBigFolderIconBlur
import com.yuk.miuiHomeR.hook.EnableBlurWhenOpenFolder
import com.yuk.miuiHomeR.hook.EnableFolderIconBlur
import com.yuk.miuiHomeR.hook.FoldDeviceDock
import com.yuk.miuiHomeR.hook.FolderAnim
import com.yuk.miuiHomeR.hook.FolderColumnsCount
import com.yuk.miuiHomeR.hook.HapticFeedback
import com.yuk.miuiHomeR.hook.HideSeekPoint
import com.yuk.miuiHomeR.hook.HideStatusBarWhenEnterRecent
import com.yuk.miuiHomeR.hook.HideWidgetTitles
import com.yuk.miuiHomeR.hook.HomeSettings
import com.yuk.miuiHomeR.hook.IconTitleColor
import com.yuk.miuiHomeR.hook.IconTitleScrolling
import com.yuk.miuiHomeR.hook.IconTitleSize
import com.yuk.miuiHomeR.hook.InfiniteScroll
import com.yuk.miuiHomeR.hook.OverlapMode
import com.yuk.miuiHomeR.hook.Recent
import com.yuk.miuiHomeR.hook.RemoveCardAnim
import com.yuk.miuiHomeR.hook.ResourcesHook
import com.yuk.miuiHomeR.hook.SetDeviceLevel
import com.yuk.miuiHomeR.hook.ShortcutBlur
import com.yuk.miuiHomeR.hook.ShortcutItemCount
import com.yuk.miuiHomeR.hook.ShortcutSmallWindow
import com.yuk.miuiHomeR.hook.ShowDockIconTitle
import com.yuk.miuiHomeR.hook.TaskViewHorizontal
import com.yuk.miuiHomeR.hook.TaskViewVertical
import com.yuk.miuiHomeR.hook.UnlockAnim
import com.yuk.miuiHomeR.hook.UnlockHotseatIcon
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.PrefsMap
import com.yuk.miuiHomeR.utils.PrefsUtils
import com.yuk.miuiHomeR.utils.ktx.atLeastAndroidS
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val TAG = "MiuiHomeR"
var mPrefsMap = PrefsMap<String, Any>()

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit /* Optional */ {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            "com.miui.home" -> {
                Application::class.java.hookBeforeMethod("attach", Context::class.java) {
                    EzXHelperInit.initHandleLoadPackage(lpparam)
                    EzXHelperInit.setLogTag(TAG)
                    EzXHelperInit.setToastTag(TAG)
                    EzXHelperInit.initAppContext(it.args[0] as Context)
                    initHooks(
                        HomeSettings,
                        ResourcesHook,
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
                        SetDeviceLevel,
                        UnlockHotseatIcon,
                        TaskViewHorizontal,
                        TaskViewVertical,
                        IconTitleSize,
                        ShowDockIconTitle,
                        InfiniteScroll,
                        HideSeekPoint,
                        AppDrawer,
                        Recent,
                        CloseFolderWhenLaunchedApp,
                        FolderAnim,
                        UnlockAnim,
                        ShortcutSmallWindow,
                        IconTitleColor,
                        IconTitleScrolling,
                        OverlapMode,
                        FoldDeviceDock,
                        ShortcutItemCount,
                        RemoveCardAnim,
                        Dock,
                        BigIconCorner,
                        HapticFeedback,
                    )
                    if (atLeastAndroidS()) {
                        initHooks(
                            ShortcutBlur,
                            EnableFolderIconBlur,
                            EnableBigFolderIconBlur,
                            AllAppsContainerViewBlur,
                        )
                    }
                }
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
                mXSharedPreferences = XSharedPreferences(Helpers.mAppModulePkg, PrefsUtils.mPrefsName)
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
}
