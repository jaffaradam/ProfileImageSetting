package com.zhihu.matisse.engine.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

public class GlideEngine4 implements ImageEngine {

//}
//public class GlideV4ImageEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(placeholder);
        requestOptions = requestOptions.override(resize, resize);
        requestOptions = requestOptions.centerCrop();
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(placeholder);
        requestOptions = requestOptions.override(resize, resize);
        requestOptions = requestOptions.centerCrop();
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.priority(Priority.HIGH);
        requestOptions = requestOptions.override(resizeX, resizeY);
        requestOptions = requestOptions.centerCrop();
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.priority(Priority.HIGH);
        requestOptions = requestOptions.override(resizeX, resizeY);
        requestOptions = requestOptions.centerCrop();
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
