package com.graypn.uid;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * 配置文件解析
 * <p>
 * Created by ZhuLei on 2017/3/9.
 * Email: zhuleineuq@gmail.com
 */

class IniParseUtils {

    /**
     * 配置文件解析
     */
    static HashMap<String, HashMap<String, String>> parseIni(File file) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset().name());
                bufferedReader = new BufferedReader(inputStreamReader);

                HashMap<String, HashMap<String, String>> propertiesMap = new HashMap<>();
                HashMap<String, String> properties = null;

                String str;
                boolean isFirstLine = true;
                while ((str = bufferedReader.readLine()) != null) {
                    str = str.trim();
                    if (isFirstLine) {
                        if (str.startsWith("\ufeff")) {
                            str = str.substring(1);
                        }
                        isFirstLine = false;
                    }
                    if (!str.startsWith("#")) {
                        if (str.startsWith("[") && str.endsWith("]")) {
                            String mapKey = str.substring(1, str.length() - 1);
                            properties = new HashMap<>();
                            propertiesMap.put(mapKey, properties);
                            continue;
                        }
                        if (str.contains("=")) {
                            int index = str.indexOf("=");
                            String key = str.substring(0, index);
                            String value = str.substring(index + 1);
                            if (!TextUtils.isEmpty(key) && properties != null) {
                                properties.put(key, value);
                            }
                        }
                    }
                }
                return propertiesMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
