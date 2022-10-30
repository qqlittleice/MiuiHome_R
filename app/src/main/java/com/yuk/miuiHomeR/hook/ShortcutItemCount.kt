package com.yuk.miuiHomeR.hook


import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod

object ShortcutItemCount : BaseHook() {
    override fun init() {
        if (!mPrefsMap.getBoolean("shortcut_remove_restrictions")) return
        findMethod("com.miui.home.launcher.shortcuts.AppShortcutMenu") { name == "getMaxCountInCurrentOrientation" }.hookAfter {
            it.result = 20
        }
        findMethod("com.miui.home.launcher.shortcuts.AppShortcutMenu") { name == "getMaxShortcutItemCount" }.hookAfter {
            it.result = 20
        }
        findMethod("com.miui.home.launcher.shortcuts.AppShortcutMenu") { name == "getMaxVisualHeight" }.hookAfter {
            it.result = it.thisObject.callMethod("getItemHeight")
        }

    }
}