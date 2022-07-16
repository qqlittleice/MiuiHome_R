package com.yuk.miuiHomeR.utils;

import moralnorm.internal.utils.DeviceHelper;

public class DeviceUtils {

    public static boolean isPad() {
        return DeviceHelper.isTablet() || DeviceHelper.isFoldDevice();
    }
}
