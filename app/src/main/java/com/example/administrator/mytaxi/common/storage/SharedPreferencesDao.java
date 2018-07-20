package com.example.administrator.mytaxi.common.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * SharedPreferencesDao保存本地数据对象，账户信息token
 */

public class SharedPreferencesDao {
    private static final String TAG = "SharedPreferencesDao";
    public static final String FILE_ACCOUNT = "FILE_ACCOUNT";
    public static final java.lang.String KEY_ACCOUNT = "KEY_ACCOUNT";
    private SharedPreferences sharedPreferences;

    /**
     * 初始化
     * @param application
     * @param fileName
     */
    public SharedPreferencesDao(Application application, String fileName) {
        sharedPreferences =
                application.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存键值对
     * @param key
     * @param value
     */
    public  void save(String key,String value){
        sharedPreferences.edit().putString(key,value).commit();
    }

    /**
     * 通过键值读取数据
     * @param key
     * @return
     */
    public String get(String key){
        return sharedPreferences.getString(key,null);
    }

    /**
     * 保存对象
     * 将对象通过Gson解析成Jason字符串保存在sharedpreference中
     */
    public void save(String key,Object object){
        String value=new Gson().toJson(object);
        save(key, value);
    }
    /**
     *  读取对象
     */

    public Object get(String key, Class cls) {

        String value = get(key);
        try {
            if (value != null) {
                Object o = new Gson().fromJson(value, cls);
                return o;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


        return null;
    }
}
