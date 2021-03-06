package com.yuk.miuiHomeR.utils.ktx

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.TypedValue
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.loadClass
import de.robv.android.xposed.XposedHelpers

fun dp2px(dpValue: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dpValue,
    InitFields.appContext.resources.displayMetrics
).toInt()

fun px2dp(pxValue: Int): Int =
    (pxValue / InitFields.appContext.resources.displayMetrics.density + 0.5f).toInt()

fun getDensityDpi(): Int =
    (InitFields.appContext.resources.displayMetrics.widthPixels / InitFields.appContext.resources.displayMetrics.density).toInt()

fun isDarkMode(): Boolean =
    InitFields.appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getProp(mKey: String): String =
    Class.forName("android.os.SystemProperties").getMethod("get", String::class.java)
        .invoke(Class.forName("android.os.SystemProperties"), mKey).toString()

fun checkVersionName(): String = InitFields.appContext.packageManager.getPackageInfo(
    InitFields.appContext.packageName, 0
).versionName

fun checkIsAlpha(): Boolean = InitFields.appContext.packageManager.getPackageInfo(
    InitFields.appContext.packageName, 0
).versionName.contains("ALPHA", ignoreCase = true)

fun checkMiuiVersion(): String = when (getProp("ro.miui.ui.version.name")) {
    "V130" -> "13"
    "V125" -> "12.5"
    "V12" -> "12"
    "V11" -> "11"
    "V10" -> "10"
    else -> "?"
}

fun checkAndroidVersion(): String = getProp("ro.build.version.release")

fun checkVersionCode(): Long = InitFields.appContext.packageManager.getPackageInfo(
    InitFields.appContext.packageName, 0
).longVersionCode