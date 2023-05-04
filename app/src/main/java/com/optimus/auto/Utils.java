package com.optimus.auto;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.ContextCompat;

/* loaded from: classes2.dex */
public class Utils {
    public static boolean isPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0;
    }
}
