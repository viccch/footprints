package io.github.viccch.footprints;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.ui.find.Blog;
import io.github.viccch.footprints.ui.me.UserInfo;
import io.github.viccch.footprints.utils.FileUtils;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppCommunicationManager {

    public static final String ServerUrl = APP.getInstance().getServerUrl();

    //上传文件
    public static void PublishBlog(String blog_user_id, String blog_title, String blog_content) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder =
                HttpUrl.get(ServerUrl + "/insert_blog")
                        .newBuilder()
                        .addQueryParameter("blog_user_id", blog_user_id)
                        .addQueryParameter("blog_title", blog_title)
                        .addQueryParameter("blog_content", blog_content);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //上传文件
    public static String Upload(Uri uri) {

        String url = null;

        try {
            File fp = new File(FileUtils.getFilePathByUri(APP.getInstance().getApplicationContext(), uri));
            String result = AppCommunicationManager.UploadFile(fp);
            JSONObject jsonObject = new JSONObject(result);
            url = APP.getInstance().getServerUrl() + jsonObject.getJSONObject("data").getString("url");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    //根据id查询用户
    public static String QueryBlogByID(String user_id) {

        final String[] str = {null};

        //https://juejin.cn/post/7068162792154464264
        //OkHttp的完整指南

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/query_blog").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", user_id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                str[0] = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str[0];
    }

    public static String QueryBlogAll() {

        final String[] str = {null};

        //https://juejin.cn/post/7068162792154464264
        //OkHttp的完整指南

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/query_blog_all").newBuilder();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                str[0] = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str[0];
    }

    //注册用户
    public static void AddUser(UserInfo info) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/insert_user").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", info.getUser_id());
        queryUrlBuilder.addQueryParameter("user_password", info.getUser_password());

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //取消关注
    public static void RemoveBlog(String id) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/remove_blog").newBuilder();
        queryUrlBuilder.addQueryParameter("id", id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取消关注
    public static void RemoveSubscribe(String my_id, String other_id) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/remove_subscribe").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", my_id);
        queryUrlBuilder.addQueryParameter("user_subscribe", other_id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加关注
    public static void AddSubscribe(String my_id, String other_id) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/insert_subscribe").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", my_id);
        queryUrlBuilder.addQueryParameter("user_subscribe", other_id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据id查询用户subscribe
    public static String QueryUserSubscribe(String user_id) {

        final String[] str = {null};

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/query_subscribe").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", user_id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                str[0] = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str[0];
    }


    //根据id查询用户
    public static String QueryUserInfoById(String user_id) {

        final String[] str = {null};

        //https://juejin.cn/post/7068162792154464264
        //OkHttp的完整指南

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(ServerUrl + "/query_user").newBuilder();
        queryUrlBuilder.addQueryParameter("user_id", user_id);

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();

        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                str[0] = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str[0];
    }

    //上传文件
    public static String UploadFile(File fp) {
        final String[] str = {null};

        //okhttp3上传图片
        //https://blog.csdn.net/itobot/article/details/85568570

        RequestBody fileBody = RequestBody.create(fp, MediaType.parse("application/octet-stream"));

        //创建MultiparBody,给RequestBody进行设置
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "admin")
                .addFormDataPart("project_id", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd")))
                .addFormDataPart("file", fp.getName(), fileBody)
                .build();

        //创建Request
        Request request = new Request.Builder()
                .url(ServerUrl + "/upload_file")
                .post(multipartBody)
                .build();

        //创建okHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(8, TimeUnit.SECONDS)
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .build();

        //创建call对象
        Thread th = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                str[0] = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            th.start();
            th.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str[0];
    }
}
