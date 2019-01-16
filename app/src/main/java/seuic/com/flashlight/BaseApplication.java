package seuic.com.flashlight;

import android.app.Application;


public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //保存本地变量
        instance = this;
    }
}
