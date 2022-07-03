package com.yuk.miuiHomeR.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.lang.reflect.Field;

public class PrefsUtils {

    public static String mPrefsName = "MiuiHome_Config";
    public static String mPrefsPathCurrent = null;
    public static String mPrefsFileCurrent = null;
    public static SharedPreferences mSharedPreferences = null;
    public static String mPrefsPath = "/data/user_de/0/" + Helpers.mAppModulePkg + "/shared_prefs";
    public static String mPrefsFile = mPrefsPath + "/" + mPrefsName + ".xml";

    public static SharedPreferences getSharedPrefs(Context context, boolean protectedStorage, boolean multiProcess) {
        if (protectedStorage) context = Helpers.getProtectedContext(context);
        try {
            return context.getSharedPreferences(mPrefsName, multiProcess ? Context.MODE_MULTI_PROCESS | Context.MODE_WORLD_READABLE : Context.MODE_WORLD_READABLE);
        } catch (Throwable t) {
            return context.getSharedPreferences(mPrefsName, multiProcess ? Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE : Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSharedPrefs(Context context, boolean protectedStorage) {
        return getSharedPrefs(context, protectedStorage, false);
    }

    public static String getSharedPrefsPath() {
        if (mPrefsPathCurrent == null) try {
            Field mFile = mSharedPreferences.getClass().getDeclaredField("mFile");
            mFile.setAccessible(true);
            mPrefsPathCurrent = ((File)mFile.get(mSharedPreferences)).getParentFile().getAbsolutePath();
            return mPrefsPathCurrent;
        } catch (Throwable t) {
            System.out.print("Test" + t);
            return mPrefsPath;
        } else return mPrefsPathCurrent;
    }

    public static String getSharedPrefsFile() {
        if (mPrefsFileCurrent == null) try {
            Field fFile = mSharedPreferences.getClass().getDeclaredField("mFile");
            fFile.setAccessible(true);
            mPrefsFileCurrent = ((File)fFile.get(mSharedPreferences)).getAbsolutePath();
            System.out.println("Test: mPrefsFileCurrent");
            return mPrefsFileCurrent;
        } catch (Throwable t) {
            System.out.println("Test: mPrefsFile" + t);
            return mPrefsFile;
        } else
            System.out.println("Test: mPrefsFileCurrent2");
        return mPrefsFileCurrent;
    }
}
