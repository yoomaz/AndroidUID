package com.graypn.demo_userid;

import android.app.Application;
import android.util.Log;

import com.graypn.uid.UidPersistenceHelper;

/**
 * Created by ZhuLei on 2017/9/28.
 * Email: zhuleineuq@gmail.com
 */

public class DemoApplication extends Application {

    private static final String TAG = "DemoApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        long start = System.currentTimeMillis();

        UidPersistenceHelper.init(this, "demo_app");

        // 耗时在 100ms 左右
        long duration = System.currentTimeMillis() - start;

        Log.i(TAG, "onCreate: " + duration);
    }
}
