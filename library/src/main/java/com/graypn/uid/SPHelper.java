package com.graypn.uid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZhuLei on 2017/9/27.
 * Email: zhuleineuq@gmail.com
 */

class SPHelper {

    private Context mContext;
    private String mSpName;
    private SharedPreferences mSharedPreferences;

    private SPHelper(Context context, String spName) {
        mContext = context;
        mSpName = spName;
        mSharedPreferences = mContext.getSharedPreferences(mSpName, Context.MODE_PRIVATE);
    }

    static SPHelper getInstance(Context context, String name) {
        return new SPHelper(context, name);
    }


    void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }
}
