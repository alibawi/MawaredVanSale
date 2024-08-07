package com.mawared.update;

import android.content.Context;
import android.util.Log;

public class UpdateChecker {


    public static void checkForDialog(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }

    public static void checkForSilent(Context context){
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, false)
                    .execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }

}
