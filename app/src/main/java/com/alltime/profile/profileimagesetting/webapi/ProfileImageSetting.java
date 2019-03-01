package com.alltime.profile.profileimagesetting.webapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class ProfileImageSetting
        implements IDownloadTaskCallback {

    public enum ImageSourceType {
        Gallery, WebUrl
    }

    String TAG;
    private final String JPEG = "jpeg";


    ICallback callback;
    Context context;
    String sourceUri;
    String pathToSave;
    ImageSourceType sourceType;

    boolean isThumbnailReqd = false;

    public ProfileImageSetting(Context context, String sourceUri, ImageSourceType sourceType, String pathToSave, ICallback callback) {
        this.sourceUri = sourceUri;
        this.context = context;
        this.pathToSave = pathToSave;
        this.sourceType = sourceType;
        this.callback = callback;
        this.TAG = Util.getTAG(ProfileImageSetting.class);
    }


    public void create(boolean createThumbnail) {

        setThumbnailReqd(createThumbnail);

        switch (sourceType) {
            case Gallery:
                saveFromGalleryStream();
                break;
            case WebUrl:
                downloadFromWebUrl();

                break;
            default:
                break;
        }
    }


    private void saveFromGalleryStream() {

    }

    private void downloadFromWebUrl() {
        DownloadImageAsync downloadImageAsync = new DownloadImageAsync(context
                , this
                , sourceUri
                , pathToSave
                , "");
    }

    private void onSaved(String imagePath) {
        if (isThumbnailReqd()) {
            String thumbnailPath = createThumbnail(imagePath, pathToSave);
            if (TextUtils.isEmpty(thumbnailPath)) {
                AppImage appImage = new AppImage();
                appImage.setImagePath(imagePath);
                doCallback(appImage);
            } else {
                AppImage appImage = new AppImage();
                appImage.setImagePath(imagePath);
                appImage.setThumbnailPath(thumbnailPath);
                doCallback(appImage);
            }

        } else {
            AppImage appImage = new AppImage();
            appImage.setImagePath(imagePath);
            doCallback(appImage);
        }
    }


    private String createThumbnail(String imagePath, String thumbnailFolder) {

        Bitmap bitmap;

        File inFile = new File(imagePath);
        InputStream input = null;

        try{
            input = new FileInputStream(inFile);
        }catch (FileNotFoundException ex){
            Log.e(TAG, "EXCEPTION: ", ex);
            return "";
        }

        Long tsLong = Util.getCurrentTime();
        String file_name = tsLong.toString();
        final String IMGNAME = file_name + JPEG;

        File outFile = new File(thumbnailFolder, IMGNAME);

        if(input != null){
            bitmap = BitmapFactory.decodeStream(input);

            OutputStream output = null;
            try{
                output = new FileOutputStream(outFile);
            }catch (FileNotFoundException ex){
                Log.e(TAG, "EXCEPTION: ", ex);
                return "";
            }

            if(output != null){
                try {
                    // Compress into png format image from 0% - 100%
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                }catch (Exception ex){
                    Log.e(TAG, "EXCEPTION: ", ex);
                    return "";
                }
            }

        }

        return outFile.toString();
    }

    public void setThumbnailReqd(boolean thumbnailReqd) {
        this.isThumbnailReqd = thumbnailReqd;
    }

    public boolean isThumbnailReqd() {
        return isThumbnailReqd;
    }

    private void doCallback(String displayErrorMessage) {
        callback.onCallback(null, displayErrorMessage);
    }

    private void doCallback(AppImage appImage) {
        callback.onCallback(appImage, "");
    }

    @Override
    public void onDownloadFailure(DownloadImageAsync.FailureReasons failureMessage, String itemReferenceId) {
        doCallback("Unable to download the image");
    }

    @Override
    public void onDownloadSuccess(String localFileWithPath, String itemReferenceId) {
        onSaved(localFileWithPath);
    }

    public interface ICallback {
        void onCallback(AppImage savedAppImage, String displayErrorMessage);
    }

}
