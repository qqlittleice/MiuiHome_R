package com.yuk.miuiHomeR.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.yuk.miuiHomeR.utils.PrefsUtils;

public class KillSelfService extends Service {

    private Handler mHandler;
    private String mLocale;
    private String mPackageName;

    //关闭应用后多久重新启动
    private static long stopDelayed = 50;

    public KillSelfService() {
        PrefsUtils.mSharedPreferences.edit().putString("prefs_key_language", mLocale);
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 50);
        mLocale = intent.getStringExtra("Locale");
        mPackageName = intent.getStringExtra("PackageName");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //杀死整个进程
                android.os.Process.killProcess(android.os.Process.myPid());
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(mPackageName);
                startActivity(LaunchIntent);
                stopSelf();
                try {

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
