package com.yuk.miuiHomeR.hook

import android.content.Context
import android.os.Message
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook

object FoldDeviceDock : BaseHook() {
    override fun init() {
        if (!mPrefsMap.getBoolean("home_fold_dock")) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        var hook3: XC_MethodHook.Unhook? = null
        "com.miui.home.launcher.hotseats.HotSeats".hookBeforeMethod("initContent") {
            hook1 = "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
                "isFoldDevice"
            ) {
                it.result = true
            }
        }
        "com.miui.home.launcher.hotseats.HotSeats".hookBeforeMethod("updateContentView") {
            hook2 = "com.miui.home.launcher.Application".hookBeforeMethod(
                "isInFoldLargeScreen"
            ) {
                it.result = true
            }
        }

        "com.miui.home.launcher.hotseats.HotSeatsListRecentsAppProvider\$1".hookBeforeMethod("handleMessage", Message::class.java) {
            hook3 = "com.miui.home.launcher.Application".hookBeforeMethod(
                "isInFoldLargeScreen"
            ) {
                it.result = true
            }
        }

        "com.miui.home.launcher.hotseats.HotSeats".hookAfterMethod("initContent") {
            hook1?.unhook()
        }

        "com.miui.home.launcher.hotseats.HotSeats".hookAfterMethod("updateContentView") {
            hook2?.unhook()
        }

        "com.miui.home.launcher.hotseats.HotSeatsListRecentsAppProvider\$1".hookAfterMethod("handleMessage", Message::class.java) {
            hook3?.unhook()
        }

        "com.miui.home.launcher.DeviceConfig".hookAfterMethod("getHotseatMaxCount") {
            it.result = 2
        }

        "com.miui.home.launcher.hotseats.HotSeatsListRecentsAppProvider".hookBeforeMethod("getLimitCount") {
            "com.miui.home.launcher.hotseats.HotSeatsList".hookAfterMethod("shouldShowSearchIcon", Context::class.java) { hookParam ->
                it.result = if (hookParam.result as Boolean) 2 else 3
            }
        }

        "com.miui.home.launcher.allapps.LauncherMode".hookBeforeMethod("isHomeSupportSearchBar", Context::class.java) {
            it.result = false
        }

    }
}