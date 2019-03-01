package com.alltime.profile.profileimagesetting.webapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageAsync extends AsyncTask<Void, Void, Void> {

    public enum FailureReasons {
        GeneralFailure,
        MalformedURLException,
        IOException
    }

    IDownloadTaskCallback downloadTaskCallback;
    Context context;
    String imageUrl;
    String downloadPath;
    String itemReferenceId;
    String TAG = "DownloadImageAsync";

    private final String JPEG = "jpeg";

    public DownloadImageAsync(Context context, IDownloadTaskCallback downloadTaskCallback, String Url, String downloadPath, String itemReferenceId) {
        this.context = context;
        this.downloadTaskCallback = downloadTaskCallback;
        this.imageUrl = Url;
        this.downloadPath = downloadPath;
        this.itemReferenceId = itemReferenceId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL url = null;
        OutputStream output;
        Bitmap bitmap;

        Long tsLong = Util.getCurrentTime();
        String file_name = tsLong.toString();
        final String IMGNAME = file_name + JPEG;
        File file = new File(downloadPath, IMGNAME);
        try {
            url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            output = new FileOutputStream(file);
            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();

            downloadTaskCallback.onDownloadSuccess(file.toString(), itemReferenceId);

        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
            downloadTaskCallback.onDownloadFailure(FailureReasons.MalformedURLException, itemReferenceId);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            downloadTaskCallback.onDownloadFailure(FailureReasons.IOException, itemReferenceId);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            downloadTaskCallback.onDownloadFailure(FailureReasons.GeneralFailure, itemReferenceId);
        }

        //just for method signature compliance
        return null;
    }

}

