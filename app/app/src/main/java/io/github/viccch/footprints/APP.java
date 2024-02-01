package io.github.viccch.footprints;

import android.app.Application;

import io.github.viccch.footprints.ui.me.UserInfo;

public class APP extends Application {
    private UserInfo user;
    private static APP instance;

    public String ip="";
    public String port="";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
//        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }

    public static APP getInstance() {
        return instance;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getServerUrl() {
//        return "http://" + "192.168.31.22" + ":" +"3000";
        return "http://" + ip + ":" +port;
    }

//    // 创建服务用于捕获崩溃异常
//    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
//        public void uncaughtException(Thread thread, Throwable ex) {
//            restartApp();//发生崩溃异常时,重启应用
//        }
//    };
//    //重启App
//    public void restartApp(){
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        this.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
//    }
}
