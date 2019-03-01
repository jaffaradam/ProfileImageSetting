package com.alltime.profile.profileimagesetting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine4;
import com.zhihu.matisse.filter.Filter;


public class MainActivity extends AppCompatActivity {

    final public static int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    final public static int PERMISSIONS_REQUEST_CAMERA = 100;
    final public static int PERMISSION_REQUEST_READ_EXTERNAL = 122;
    final public static int PERMISSION_REQUEST_WRITE_EXTERNAL = 124;


    ImageView background_image;
    private TextView textview;
    String selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        background_image = (ImageView) findViewById(R.id.test_image);
        textview = (TextView)findViewById(R.id.txtComment);

        setSupportActionBar(toolbar);



        background_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!FileSystemUtil.hasStoragePermissions(MainActivity.this))
                    FileSystemUtil.askStoragePermission(MainActivity.this);
                else {
                    /*Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/* video/*");
                    startActivityForResult(pickIntent, PICK_IMAGE_GALLERY);*/

                    Matisse.from(MainActivity.this)
                            .choose(MimeType.ofAll())
                            .countable(true)
                            .maxSelectable(9)
                            //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                            //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine4())
                            .forResult(PICK_IMAGE_GALLERY);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_REQUEST_CAMERA:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                }
                break;

            case PERMISSION_REQUEST_WRITE_EXTERNAL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    background_image.callOnClick();
                }
                break;

            case PERMISSION_REQUEST_READ_EXTERNAL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/* video/*");
                    startActivityForResult(pickIntent, PICK_IMAGE_GALLERY);

//                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    photoPickerIntent.setType("*/*");
//                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/* video/*"});
//                    startActivityForResult(photoPickerIntent, PICK_IMAGE_GALLERY);
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_IMAGE_GALLERY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                selectedList = data.getExtras().getStringArrayList("extra_result_selection_path").get(0);

                String setValue = data.getExtras().getString("comment");
                textview.setText(setValue);

                String mediatype = data.getExtras().getString("media_type");
                Toast.makeText(this, mediatype, Toast.LENGTH_SHORT).show();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                RequestOptions options = new RequestOptions()
                        .error(com.zhihu.matisse.R.drawable.ic_photo_camera_white_24dp)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 3);


                    Glide.with(this)
                            .load(selectedList)
                            .apply(options)
                            .into(background_image);


//Log.e("select_path", imagePath);
            }
        }
    }


    protected boolean isCloudFile(String imagePath) {
        return TextUtils.isEmpty(imagePath);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {

        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("ACTIVITY", "EXCEPTION", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getImagePathFromInputStreamUri(Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        try{
            if (uri.getAuthority() != null) {
                try {
                    inputStream = MainActivity.this.getContentResolver().openInputStream(uri); // context needed
                    File photoFile = createTemporalFileFrom(inputStream);

                    filePath = photoFile.getPath();

                } catch (FileNotFoundException e) {
                    // log
                } catch (IOException e) {
                    // log
                }finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }


        return filePath;
    }

    private File createTemporalFileFrom(InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile();
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private File createTemporalFile() {
        return new File(MainActivity.this.getExternalCacheDir(), "tempFile.jpg"); // context needed
    }


    private void displayImage(String localProfileImageUri){

        if(!TextUtils.isEmpty(localProfileImageUri)){
            DisplayMetrics displayMEtrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMEtrics);

            RequestOptions options = new RequestOptions()
                    .error(R.drawable.avatar_generic)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(displayMEtrics.widthPixels / 2, displayMEtrics.heightPixels / 3);

            Glide.with(MainActivity.this)
                    .load(localProfileImageUri)
                    .apply(options)
                    .into(background_image);
        }
    }
}
