package com.alltime.profile.profileimagesetting.webapi;

public interface IDownloadTaskCallback {

    void onDownloadFailure(DownloadImageAsync.FailureReasons failureMessage, String itemReferenceId);
    void onDownloadSuccess(String localFileWithPath, String itemReferenceId);
}