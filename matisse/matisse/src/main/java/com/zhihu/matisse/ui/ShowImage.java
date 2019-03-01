package com.zhihu.matisse.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.R;

import java.util.ArrayList;

import it.sephiroth.android.library.easing.Linear;

public class ShowImage extends AppCompatActivity {

    TextView textview;
    ArrayList<String> selectedList = new ArrayList<>();
    private String PATH = "";
    ImageView previewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_send_preview);

//        textview = (TextView)findViewById(R.id.textview1);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selectedList = null;
            }else {
                selectedList = extras.getStringArrayList("extra_result_selection_path");
                Log.e("input list count: ", String.valueOf(selectedList.size()));
            }
        }

        previewImage = this.findViewById(R.id.preview_image);

        /*File imagFile = new File("/storage/emulated/0/Pictures/Screenshots/Screenshot_20180802-220045.jpg");
        if(imagFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imagFile.getAbsolutePath());
            previewImage.setImageBitmap(bitmap);
        }*/


        PATH = selectedList.get(0);
        Log.e("Path",PATH);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_photo_camera_white_24dp)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 3);


        for(int i=0; i< selectedList.size(); i++) {
            Glide.with(this)
                    .load(selectedList.get(i))
                    .apply(options)
                    .into(previewImage);

        }

        /*Glide.with(this)
                .load(PATH)
                .into(previewImage);*/

      //  textview.setText(path);
    }
}
