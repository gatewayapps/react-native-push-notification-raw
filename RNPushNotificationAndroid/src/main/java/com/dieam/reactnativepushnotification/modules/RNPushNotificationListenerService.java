package com.dieam.reactnativepushnotification.modules;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.util.List;

import com.google.android.gms.gcm.GcmListenerService;

public class RNPushNotificationListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        sendNotification(bundle);
    }

    private void sendNotification(Bundle bundle) {

        Boolean isRunning = isApplicationRunning();
        
        Intent intent = new Intent("RNPushNotificationReceiveNotification");
        bundle.putBoolean("foreground", isRunning);
        intent.putExtra("notification", bundle);
        sendBroadcast(intent);

        if (!isRunning) {
            new RNPushNotificationHelper(getApplication(), this).sendNotification(bundle);
        }
    }

    private boolean isApplicationRunning() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName.equals(getApplication().getPackageName())) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND || processInfo.importance == 300 || processInfo.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    for (String d: processInfo.pkgList) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
