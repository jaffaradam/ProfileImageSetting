package com.alltime.profile.profileimagesetting;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSystemUtil {

    final static private int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 124;

    public static boolean isLocalImagePath(String imageUrl) {
        return !imageUrl.toLowerCase().startsWith("http");
    }

    public static boolean hasStoragePermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context.getPackageName());
        return (hasPerm == PackageManager.PERMISSION_GRANTED);
    }

    public static void askStoragePermission(Context context) {
        PermissionUtils.requestPermission((AppCompatActivity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_WRITE_EXTERNAL);
    }



    public static String createTodayMediaFolderIfUnavail(String mediaFolder) {
        return getTodayFolderName(mediaFolder);
    }


    private static String getTodayFolderName(String baseDirectory) {

        try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = baseDirectory + "/" + dateFormat.format(now);
            File dir = new File(time);

            // check if the directory exists:
            if (!dir.exists()) {
                dir.mkdir();
            }

            return dir.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}
