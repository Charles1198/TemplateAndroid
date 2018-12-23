package com.charles.module1.page.avatar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.charles.common.base.BaseActivity;
import com.charles.common.util.AlertUtil;
import com.charles.common.util.FileUtil;
import com.charles.module1.R;

import java.io.File;

/**
 * @author charles
 */
public class AvatarActivity extends BaseActivity {
    private final int IMAGE_RESULT = 0;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int ALBUM_REQUEST_CODE = 2;

    private ImageView avatarIv;

    private static final String AVATAR_IMG = "avatar.jpg";
    public static final String AVATAR_IMG_CROP = "avatar_crop.jpg";
    private Uri avatarUri;
    private Uri avatarUriCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module1_activity_avatar);

        initView();
    }

    private void initView() {
        avatarIv = findViewById(R.id.avatar_iv);
        findViewById(R.id.avatar_camera_btn).setOnClickListener(v -> changeAvatarFromCamera());
        findViewById(R.id.avatar_album_btn).setOnClickListener(v -> changeAvatarFromAlbum());
    }

    /**
     * 拍照换头像
     */
    private void changeAvatarFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                AlertUtil.simpleAlert(this, "您拒绝了APP使用您的相机", "您可以前往'设置'打开相机使用权限");
            }
        }
    }

    private void takePhoto() {
        File avatarImg = new File(FileUtil.getImageDirectory(), AVATAR_IMG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
            avatarUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", avatarImg);
        } else {
            //7.0以下使用这种方式创建一个Uri
            avatarUri = Uri.fromFile(avatarImg);
        }

        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra("return-data", true);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri);
        intentFromCapture.putExtra("noFaceDetection", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentFromCapture.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentFromCapture.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册选择头像
     */
    private void changeAvatarFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 拍照、选照片或裁剪图片的返回数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data != null) {
                    cropPhoto(data.getData());
                }
                break;
            case CAMERA_REQUEST_CODE:
                cropPhoto(avatarUri);
                break;
            case IMAGE_RESULT:
                if (data != null) {
                    showNewImage();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     */
    public void cropPhoto(Uri uri) {
        File avatarImgCrop = new File(FileUtil.getImageDirectory(), AVATAR_IMG_CROP);
        avatarUriCrop = Uri.fromFile(avatarImgCrop);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //裁剪之后，保存在裁剪文件中，关键
        intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUriCrop);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, IMAGE_RESULT);
    }

    /**
     * 显示新头像
     */
    public void showNewImage() {
        File cropImage = new File(FileUtil.getImageDirectory(), AVATAR_IMG_CROP);
        Bitmap avatarBitmap = BitmapFactory.decodeFile(cropImage.getPath());
        avatarIv.setImageBitmap(avatarBitmap);
    }
}
