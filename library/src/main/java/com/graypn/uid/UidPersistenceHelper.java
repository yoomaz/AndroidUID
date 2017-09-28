package com.graypn.uid;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ZhuLei on 2017/9/26.
 * Email: zhuleineuq@gmail.com
 */

public class UidPersistenceHelper {

    // 缓存文件名称
    private static final String CONFIG_FILE_NAME = "cache_uid_0520.ini";
    // user 字段名称
    private static final String CONFIG_USER_INFO_NAME = "user";
    // SP 文件名称
    private static final String CONFIG_USER_INFO_SP_NAME = "cache_uid_info";
    // SP 中 UID 字段key
    private static final String CONFIG_USER_INFO_UID_NAME = "uid";

    private Context mContext;
    private SPHelper mSPHelper;
    private static volatile String mUid;

    public UidPersistenceHelper(Context context, String appFolderName) {
        mContext = context;
        mSPHelper = SPHelper.getInstance(context, CONFIG_USER_INFO_SP_NAME);
        AppEnvironment.setFolderName(appFolderName);
        initUid();
    }

    /**
     * 初始化 Uid
     */
    private void initUid() {
        // 首先从 sp 中读取，然后从sd卡中读取
        mUid = mSPHelper.getString(CONFIG_USER_INFO_UID_NAME);
        if (TextUtils.isEmpty(mUid)) {
            mUid = queryUidFromSdCard();
        }
        // 如果读取的 uid 是空的或者第一次安装app
        if (TextUtils.isEmpty(mUid)) {
            mUid = UUID.randomUUID().toString();
            persistentUid(mUid);
        }
    }

    /**
     * 获取用户 ID
     */
    public static String getUid() {
        return mUid;
    }

    /**
     * 从 sd 卡中读取 uid
     */
    private String queryUidFromSdCard() {
        String uid;
        // 内部存储
        String internalFilePath = AppEnvironment.getInternalStorageDirPath(mContext) + File.separator + CONFIG_FILE_NAME;
        uid = queryUid(internalFilePath);
        if (!TextUtils.isEmpty(uid)) {
            return uid;
        }
        // 外部存储隐藏目录
        String externalHideFilePath = AppEnvironment.getAppExternalStorageDirPath(true) + CONFIG_FILE_NAME;
        uid = queryUid(externalHideFilePath);
        if (!TextUtils.isEmpty(uid)) {
            return uid;
        }
        // 外部存储隐藏目录
        String externalFilePath = AppEnvironment.getAppExternalStorageDirPath(false) + CONFIG_FILE_NAME;
        uid = queryUid(externalFilePath);
        if (!TextUtils.isEmpty(uid)) {
            return uid;
        }
        return null;
    }

    /**
     * 从文件中查询 UID
     */
    private String queryUid(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            HashMap<String, HashMap<String, String>> maps = IniParseUtils.parseIni(file);
            if (maps != null) {
                HashMap<String, String> userInfo = maps.get(CONFIG_USER_INFO_NAME);
                if (userInfo != null) {
                    return userInfo.get(CONFIG_USER_INFO_UID_NAME);
                }
            }
        }
        return null;
    }

    /**
     * 持久化 uid ：SharedPreference，内部存储，外部存储(非隐藏目录，隐藏目录)
     */
    private void persistentUid(String uid) {
        // SharedPreference 存储
        mSPHelper.putString(CONFIG_USER_INFO_UID_NAME, uid);
        // 内部存储
        String internalFilePath = AppEnvironment.getInternalStorageDirPath(mContext) + File.separator + CONFIG_FILE_NAME;
        writeUidToFile(internalFilePath, uid);
        // 外部存储(隐藏目录)
        String externalHideFilePath = AppEnvironment.getAppExternalStorageDirPath(true) + CONFIG_FILE_NAME;
        writeUidToFile(externalHideFilePath, uid);
        // 外部存储(非隐藏目录)
        String externalFilePath = AppEnvironment.getAppExternalStorageDirPath(false) + CONFIG_FILE_NAME;
        writeUidToFile(externalFilePath, uid);
    }

    /**
     * 写入 UID
     *
     * @param filePath 文件路径
     * @param uid      uid
     */
    private void writeUidToFile(String filePath, String uid) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);

            osw.write(mContext.getString(R.string.ini_export_tips));
            osw.write(String.format("[%s]\r\n", "user"));
            osw.write(String.format("%s=%s\r\n", "uid", uid));
            osw.write("\r\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void init(Context context, String appFolderName) {
        new UidPersistenceHelper(context, appFolderName);
    }
}
