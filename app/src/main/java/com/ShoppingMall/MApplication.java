package com.ShoppingMall;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


/**
 * Created by aaron on 16/9/7.
 */

public class MApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56f4c1dd");
    }
}
