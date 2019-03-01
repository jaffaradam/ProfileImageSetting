package com.alltime.profile.profileimagesetting.webapi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AppSetupHelper {

    Activity activity;
    Context context;
    boolean hasStoragePersmission;

    private final String JPEG = "jpeg";


    /*String localProfilePic;
    String socialProfileImageUrl;
    private final String UPLOAD_PROFILE_IMG_FAILED_CONST = "FAILED";
    private boolean imageDownloadFailed = false; */

    String TAG;

    //    private boolean localProfileImageSetupFailed = false;
    IAppSetupTracker setupTracker;

    public AppSetupHelper(IAppSetupTracker setupTracker, Activity activity, boolean hasStoragePersmission, String socialProfileImageUrl) {
        this.activity = activity;
        this.context = activity;
        this.setupTracker = setupTracker;
        //hasStoragePermission parameter is to instill developer awareness; not useful otherwise
        this.hasStoragePersmission = hasStoragePersmission;
        /*this.socialProfileImageUrl = socialProfileImageUrl;*/
        TAG = Util.getTAG(AppSetupHelper.class);
    }


    public void doSetup() {
        if (hasStoragePersmission) {
            populateFolderPaths();

            if (appRootFolderPath == null || appRootFolderPath.isEmpty()) {
                setupTracker.onFailedAppSetup();
                return;
            } else {
                setupTracker.onCompleteAppSetup();
            }


        }
    }


    String appRootFolderPath, settingsFolder, profileFolder, mediaFolder;

    private void populateFolderPaths() {

        if (TextUtils.isEmpty(appRootFolderPath))
            appRootFolderPath = getAppRootFolderPath();
        if (TextUtils.isEmpty(settingsFolder))
            settingsFolder = getSettingsFolderPath();
        if (TextUtils.isEmpty(profileFolder))
            profileFolder = getProfileFolderPath();
        if (TextUtils.isEmpty(mediaFolder))
            mediaFolder = getMediaFolderPath();

        createFoldersForApp(appRootFolderPath);
    }

    public String getSettingsFolderPath() {
        return getSettingsFolderPath(getAppRootFolderPath());
    }

    public String getProfileFolderPath() {
        return getProfileFolderPath(getSettingsFolderPath());
    }

    public String getMediaFolderPath() {
        return getMediaFolderPath(getAppRootFolderPath());
    }


    private String getSettingsFolderPath(String appRootFolderPath) {
        return new File(appRootFolderPath + "/" + "settings").getAbsolutePath();
    }

    private String getProfileFolderPath(String settingsFolder) {
        return new File(settingsFolder + "/" + "profile").getAbsolutePath();
    }

    private String getMediaFolderPath(String appRootFolderPath) {
        return new File(appRootFolderPath + "/" + "media").getAbsolutePath();
    }


    public String getAppRootFolderPath() {
        String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new File(extStorageDirectory + "/" + "test").getAbsolutePath();
    }



    public void createFoldersForApp(String appRootFolder) {
        String appRootFolderPath = appRootFolder;
        String settingsFolder = getSettingsFolderPath(appRootFolderPath);
        String profileFolder = getProfileFolderPath(settingsFolder);
        String mediaFolder = getMediaFolderPath(appRootFolderPath);

        //initialize folders where images will be created
        File appRootPath = new File(appRootFolderPath);
        File settingPath = new File(settingsFolder);
        File profilePath = new File(profileFolder);
        File mediaPath = new File(mediaFolder);

        if (!appRootPath.exists()) {
            try {
                appRootPath.mkdir();
            } catch (Exception e) {
            }
        } else {
        }

        Log.e("", "----->" + appRootFolderPath);

        if (!settingPath.exists()) {
            try {
                settingPath.mkdir();
            } catch (Exception e) {
            }
        } else {
        }

        if (!profilePath.exists()) {
            try {
                profilePath.mkdir();

                OutputStream output;
                File noMedia = new File(profilePath, ".NOMEDIA");
                output = new FileOutputStream(noMedia);
                output.flush();
                output.close();

            } catch (Exception e) {
                Log.e(TAG, "EXCEPTION", e);

            }
        } else {
        }

        if (!mediaPath.exists()) {
            try {
                mediaPath.mkdir();
            } catch (Exception e) {
            }
        } else {
        }

    }



    private String copyPicToProfileFolder(String localImagePath) {

        OutputStream outStream;
        OutputStream output;
        Long tsLong = Util.getCurrentTime();
        String file_name = tsLong.toString();
        final String IMGNAME = file_name + JPEG;

        File imgPath = new File(profileFolder, IMGNAME);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(localImagePath));
            outStream = new FileOutputStream(imgPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            Log.e("Profile pic: ", "" + imgPath.getAbsolutePath());
            return imgPath.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }


    public interface IAppSetupTracker {
        void onCompleteAppSetup();

        void onFailedAppSetup();
    }
}

