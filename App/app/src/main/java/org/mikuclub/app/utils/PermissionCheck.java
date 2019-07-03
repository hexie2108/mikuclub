package org.mikuclub.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * class che gestisce il problema di permesso
 * source: https://www.jianshu.com/p/b4a8b3d4f587
 */
public class PermissionCheck
{

        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private static String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        public static void verifyStoragePermissions(Activity activity)
        {
                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // if we don't have permission
                if (permission != PackageManager.PERMISSION_GRANTED)
                {
                        //prompt the user
                        ActivityCompat.requestPermissions(
                                activity,
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );
                }
        }
}
