package io.github.viccch.footprints.ui.record;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.utils.FileUtils;

public class SelectPictureActivity extends AppCompatActivity {

    public enum REQUEST_CODE {
        //        SELECT_PIC,// 选取一张照片
//        SELECT_PICS,// 选取多张照片
        TAKE_PICTURE,//拍照
        //        TAKE_VIDEO,//录像
        UPLOAD_PICTURE,//上传PICTURE
        UPLOAD_VIDEO,//上传VIDEO
    }

    Button btn_upload_picture;
    Button btn_upload_video;
    //    Button btn_select_pictures;
    Button btn_take_picture;
    Button btn_call_camera;
    ImageView img_test;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpicture);

        textView2 = findViewById(R.id.textView2);
        img_test = findViewById(R.id.img_test);

//        btn_take_picture = findViewById(R.id.btn_take_picture);
//        btn_take_picture.setOnClickListener(v -> takePicture());

        btn_call_camera = findViewById(R.id.btn_call_camera);
        btn_call_camera.setOnClickListener(v -> callCamera());

//        btn_select_pictures = findViewById(R.id.btn_select_pictures);
//        btn_select_pictures.setOnClickListener(v -> selectPictures());

        btn_upload_picture = findViewById(R.id.btn_upload_picture);
        btn_upload_picture.setOnClickListener(v -> uploadPicture());

        btn_upload_video = findViewById(R.id.btn_upload_video);
        btn_upload_video.setOnClickListener(v -> uploadVideo());
    }


    private void uploadVideo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_CODE.UPLOAD_VIDEO.ordinal());
    }

    private void uploadPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE.UPLOAD_PICTURE.ordinal());
    }

    private void callCamera() {
//        startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE));
        startActivityForResult(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE), REQUEST_CODE.UPLOAD_PICTURE.ordinal());
    }

    /**
     * 调用相机拍照
     * https://blog.csdn.net/sinat_28566577/article/details/100124881
     */
    private void takePicture() {

        try {
            // 打开相机Intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);

            //这里的日期时间是打开Intent时的时间，并不是拍摄的时间，而且一定早于拍摄的时间，为了简化程序，不再对拍摄的照片进行重命名
            String imageName = "IMG_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".jpg"; // 指定名字
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), imageName); // 指定文件
            Uri fileUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", file); // 路径转换

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null

            m_file_path = fileUri.toString();

            startActivityForResult(cameraIntent, REQUEST_CODE.TAKE_PICTURE.ordinal());

//            img_test.setImageURI(fileUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String m_file_path;

    /**
     * 调用相册选择多张照片
     */
    /*
    private void selectPictures() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(intent, REQUEST_CODE.SELECT_PICS.ordinal());
    }
    */

    /**
     * 调用相册选择一张照片
     */
    /*
    private void selectPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE.SELECT_PIC.ordinal());
    }
    */

    /**
     * intent回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch ((REQUEST_CODE.values()[requestCode])) {
            /*
            case SELECT_PIC: {
                if (data == null) return;
//                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
                img_test.setImageURI(data.getData());
                break;
            }
            case SELECT_PICS: {
                if (data == null) return;
                StringBuffer stringBuffer = new StringBuffer();
                ClipData imageNames = data.getClipData();
                if (imageNames != null) {
                    for (int i = 0; i < imageNames.getItemCount(); i++) {
                        Uri imageUri = imageNames.getItemAt(i).getUri();
                        stringBuffer.append(imageUri.toString() + "\n");

                        String fileAbsolutePath = UriUtils.getFileAbsolutePath(this, imageUri);
                        GetEXIF.Result res = GetEXIF.getTimeAndLocation(fileAbsolutePath);
                        Toast.makeText(this, res.gps_latitude + "\t" + res.gps_longitude, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    stringBuffer.append(data.getData().toString());
                    String fileAbsolutePath = UriUtils.getFileAbsolutePath(this, data.getData());
                    GetEXIF.Result res = GetEXIF.getTimeAndLocation(fileAbsolutePath);
                    Toast.makeText(this, res.gps_latitude + "\t" + res.gps_longitude, Toast.LENGTH_SHORT).show();
                    img_test.setImageURI(data.getData());
                }
                textView2.setText(stringBuffer);
                break;
            }
            */
            case TAKE_PICTURE: {
                textView2.setText(m_file_path);
                img_test.setImageURI(Uri.parse(m_file_path));
                break;
            }
            case UPLOAD_PICTURE:
            case UPLOAD_VIDEO: {
                if (data == null) return;

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        Uri imageUri = clipData.getItemAt(i).getUri();

                        File fp = new File(FileUtils.getFilePathByUri(this, imageUri));

                        String result = AppCommunicationManager.UploadFile(fp);

                        String url = null;

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            url = APP.getInstance().getServerUrl() + jsonObject.getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(this, "result:" + url, Toast.LENGTH_LONG).show();

//                        GetEXIF.Result res = GetEXIF.getTimeAndLocation(fileAbsolutePath);
//                        Toast.makeText(this, res.gps_latitude + "\t" + res.gps_longitude, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String strUri = FileUtils.getFilePathByUri(this, data.getData());

                    File fp = new File(strUri);

                    String result = AppCommunicationManager.UploadFile(fp);

                    String url = null;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        url = APP.getInstance().getServerUrl() + jsonObject.getJSONObject("data").getString("url");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(this, "result:" + url, Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                break;
        }
    }
}

// 多选图片
// 注意一点，getData()和getClipData()返回的类型是不一样的，
// 如果返回的多张图片的地址，必须用getClipData()来处理。此时getData()返回的是null。
// getClipData()返回的类型可以理解为getData()返回类型的list
// https://blog.csdn.net/Number____10/article/details/105483227