package com.ShoppingMall;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ShoppingMall.utils.Constants;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import greendao.DaoMaster;
import greendao.DaoSession;


/**
 * Created by aaron on 16/9/7.
 */

public class MApplication extends Application{


    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取全局上下文
        mContext  = this;

        ZXingLibrary.initDisplayOpinion(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56f4c1dd");
    }



}
