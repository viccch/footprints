package io.github.viccch.footprints.ui.record;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.find.PlayVideoActivity;
import io.github.viccch.footprints.ui.find.ScaleImageActivity;
import io.github.viccch.footprints.utils.FileUtils;

public class TestFragment extends DialogFragment {
    public static String ARG_PARAM_CONTEXT = "content";
    public static String MIME_VIDEO = "video/*";
    public static String MIME_IMAGE = "image/*";
    View view;

    Button btn_ok;
    Button btn_cancel;
    EditText editText_title;
    EditText editText_description;

    TextView textView_lonlat;
    TextView textView_datetime;

    Button btn_shot_photo;
    Button btn_shot_video;
    Button btn_get_photo;
    Button btn_get_video;
    RecyclerView recyclerView;

    RecordFragment.Point_Content point_content;

//    List<Uri> uriList;
//    List<String> typeList;

    Uri photo_uri;
    Uri video_uri;

    //ActivityResultContracts
    //https://juejin.cn/post/7066017758885969933
    ActivityResultLauncher<Uri> launcher_shot_photo = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean success) {
            if (success) {
                point_content.uriList.add(photo_uri);
                point_content.typeList.add(MIME_IMAGE);
                initRecyclerView();
            }
        }
    });
    ActivityResultLauncher<Uri> launcher_shot_video = registerForActivityResult(new ActivityResultContracts.CaptureVideo(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean success) {
            if (success) {
                point_content.uriList.add(video_uri);
                point_content.typeList.add(MIME_VIDEO);
                initRecyclerView();
            }
        }
    });
    ActivityResultLauncher<String> launcher_get_photo = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> resultList) {
            if (resultList != null) {
                point_content.uriList.addAll(resultList);
                for (int i = 0; i < resultList.size(); i++)
                    point_content.typeList.add(MIME_IMAGE);
                initRecyclerView();
            }
        }
    });
    ActivityResultLauncher<String> launcher_get_video = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> resultList) {
            if (resultList != null) {
                point_content.uriList.addAll(resultList);
                for (int i = 0; i < resultList.size(); i++)
                    point_content.typeList.add(MIME_VIDEO);
                initRecyclerView();
            }
        }
    });

    //定义回调接口
    public interface MyListener {
        //发送数据到上一级
        void send_info(RecordFragment.Point_Content point_content);

        //设置gps，时间等信息
        void set_info();
    }

    private MyListener myListener;


    public void set_myListener(MyListener myListener) {
        this.myListener = myListener;
    }

//    void clear() {
//        point_content.title = null;
//        editText_title.setText(point_content.title);
//
//        point_content.description = null;
//        editText_description.setText(point_content.description);
//
//        point_content.uriList.clear();
//        point_content.typeList.clear();
//
//        initRecyclerView();
//    }

    void initRecyclerView() {

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        ItemAdapter itemAdapter = new ItemAdapter(point_content.uriList, point_content.typeList, this.getContext());
        recyclerView.setAdapter(itemAdapter);
    }

    public void set_info(RecordFragment.Point_Content pointContent) {
        this.point_content = pointContent;

        editText_title.setText(point_content.title);
        editText_description.setText(point_content.description);

        textView_lonlat.setText(String.format("%.6f", point_content.location.getLongitude()) + "," + String.format("%.6f", point_content.location.getLatitude()));
        textView_datetime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(point_content.location.getTime()));
    }

    public void send_info() {
        point_content.title = editText_title.getText().toString();
        point_content.description = editText_description.getText().toString();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);

        setCancelable(false);

        textView_lonlat = view.findViewById(R.id.textView_lonlat);
        textView_datetime = view.findViewById(R.id.textView_datetime_blog_pub);

        btn_ok = view.findViewById(R.id.btn_ok);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        editText_title = view.findViewById(R.id.editText_title);
        editText_description = view.findViewById(R.id.editText_description);

        btn_shot_photo = view.findViewById(R.id.btn_shot_photo);
        btn_shot_photo.setOnClickListener(v -> {
            //这里的日期时间是打开Intent时的时间，并不是拍摄的时间，而且一定早于拍摄的时间，为了简化程序，不再对拍摄的照片进行重命名
            String imageName = "IMG_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".jpg"; // 指定名字
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), imageName); // 指定文件
            photo_uri = FileProvider.getUriForFile(view.getContext(), view.getContext().getPackageName() + ".fileprovider", file); // 路径转换

            launcher_shot_photo.launch(photo_uri);
        });

        btn_shot_video = view.findViewById(R.id.btn_shot_video);
        btn_shot_video.setOnClickListener(v -> {
            //这里的日期时间是打开Intent时的时间，并不是拍摄的时间，而且一定早于拍摄的时间，为了简化程序，不再对拍摄的照片进行重命名
            String imageName = "VID_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".mp4"; // 指定名字
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), imageName); // 指定文件
            video_uri = FileProvider.getUriForFile(view.getContext(), view.getContext().getPackageName() + ".fileprovider", file); // 路径转换

            launcher_shot_video.launch(video_uri);
        });

        btn_get_photo = view.findViewById(R.id.btn_get_photo);
        btn_get_photo.setOnClickListener(v -> {
            launcher_get_photo.launch(MIME_IMAGE);
        });

        btn_get_video = view.findViewById(R.id.btn_get_video);
        btn_get_video.setOnClickListener(v -> {
            launcher_get_video.launch(MIME_VIDEO);
        });

        btn_ok.setOnClickListener(v -> {
            if (editText_title.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(this.getContext())
                        .setTitle("标题不能为空").create()
                        .show();
                return;
            }
            myListener.send_info(point_content);
            dismiss();
        });

        btn_cancel.setOnClickListener(v -> {
            dismiss();
        });


        myListener.set_info();

        initRecyclerView();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public enum REQUEST_CODE {
        //        SELECT_PIC,// 选取一张照片
//        SELECT_PICS,// 选取多张照片
        TAKE_PICTURE,//拍照
        //        TAKE_VIDEO,//录像
        UPLOAD_PICTURE,//上传PICTURE
        UPLOAD_VIDEO,//上传VIDEO
    }


    /**
     * intent回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch ((io.github.viccch.footprints.ui.record.SelectPictureActivity.REQUEST_CODE.values()[requestCode])) {
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
            case UPLOAD_PICTURE:
            case UPLOAD_VIDEO: {
                if (data == null) return;

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        Uri imageUri = clipData.getItemAt(i).getUri();

                        File fp = new File(FileUtils.getFilePathByUri(getContext(), imageUri));

                        String result = AppCommunicationManager.UploadFile(fp);

                        String url = null;

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            url = APP.getInstance().getServerUrl() + jsonObject.getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(getContext(), "result:" + url, Toast.LENGTH_LONG).show();

//                        GetEXIF.Result res = GetEXIF.getTimeAndLocation(fileAbsolutePath);
//                        Toast.makeText(this, res.gps_latitude + "\t" + res.gps_longitude, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String strUri = FileUtils.getFilePathByUri(getContext(), data.getData());

                    File fp = new File(strUri);

                    String result = AppCommunicationManager.UploadFile(fp);

                    String url = null;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        url = APP.getInstance().getServerUrl() + jsonObject.getJSONObject("data").getString("url");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(getContext(), "result:" + url, Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                break;
        }
    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private final List<Uri> uriList;
        private final List<String> typeList;
        private final Context context;

        public ItemAdapter(List<Uri> uriList, List<String> typeList, Context context) {
            this.typeList = typeList;
            this.uriList = uriList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(this.context).inflate(R.layout.item_image, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Uri uri = uriList.get(position);
            String type = typeList.get(position);
            if (type.equals(MIME_VIDEO)) {
                holder.btn_play.setVisibility(View.VISIBLE);
            } else if (type.equals(MIME_IMAGE)) {
            }
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .into(holder.imageView);
            holder.btn_close.setOnClickListener(v -> {
                uriList.remove(position);
                typeList.remove(position);
                initRecyclerView();
            });

            holder.itemView.setOnClickListener(v -> {
//                startActivity(new Intent(Intent.ACTION_VIEW)
//                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        .setDataAndType(uri, type));

                if (type.equals(MIME_VIDEO)) {
                    startActivity(new Intent(view.getContext(), PlayVideoActivity.class).putExtra(PlayVideoActivity.M_ARG_PARAM_URL, uri.toString()));
                } else if (type.equals(MIME_IMAGE)) {
                    startActivity(new Intent(view.getContext(), ScaleImageActivity.class).putExtra(ScaleImageActivity.M_ARG_PARAM_URL, uri.toString()));
                }


            });
        }

        @Override
        public int getItemCount() {
            return uriList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            Button btn_close;
            ImageView imageView;
            ViewGroup btn_play;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                btn_play = itemView.findViewById(R.id.btn_play);
                btn_close = itemView.findViewById(R.id.btn_close);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }

    }
}
