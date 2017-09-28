package com.graypn.uid;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by ZhuLei on 2017/9/26.
 * Email: zhuleineuq@gmail.com
 */

class AppEnvironment {

    private static String sdcard_dir = "demo";
    private static String sdcard_dir_hidden = ".demo";

    /**
     * 获取 App 存储目录
     * @param isHiddenDir 是否是隐藏文件夹
     */
    static String getAppExternalStorageDirPath(boolean isHiddenDir) {
        String dirName = isHiddenDir ? sdcard_dir_hidden : sdcard_dir;
        String path = getExternalStorageDirPath() +
                File.separator +
                dirName +
                File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    /**
     * 获取系统 sd 卡路径
     */
    static String getExternalStorageDirPath() {
        try {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } catch (Throwable var1) {
            return (new File("/sdcard/")).getAbsolutePath();
        }
    }

    /**
     * 获取应用内部存储路径
     */
    static String getInternalStorageDirPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }


    static void setFolderName(String folderName) {
        sdcard_dir = folderName;
        sdcard_dir_hidden = "." + folderName;
    }
}
