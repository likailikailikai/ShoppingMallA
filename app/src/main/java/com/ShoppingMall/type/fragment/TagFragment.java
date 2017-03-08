package com.ShoppingMall.type.fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ShoppingMall.R;
import com.ShoppingMall.base.BaseFragment;
import com.ShoppingMall.type.adapter.TagGridViewAdapter;
import com.ShoppingMall.type.bean.TagBean;
import com.ShoppingMall.type.view.FlowLayout;
import com.ShoppingMall.utils.Constants;
import com.ShoppingMall.utils.DensityUtil;
import com.ShoppingMall.utils.DrawUtils;
import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by 情v枫 on 2017/3/3.
 * <p>
 * 作用：
 */

public class TagFragment extends BaseFragment {

    @InjectView(R.id.fl_tag)
    FlowLayout flTag;
    private List<TagBean.ResultEntity> result;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_tag, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /**
     * 1.把数据绑定到控件上的时候，重新该方法
     * 2.联网请求，把得到的数据绑定到视图上
     */
    @Override
    public void initData() {
        super.initData();
        getDataFromNet(Constants.TAG_URL);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "TagFragment联网失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void processData(String json) {

        TagBean tagBean = JSON.parseObject(json,TagBean.class);
        result = tagBean.getResult();
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                final TextView tv = new TextView(getContext());

                //设置属性
                tv.setText(result.get(i).getName());
                ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mp.leftMargin = DensityUtil.dip2px(mContext, 5);
                mp.rightMargin = DensityUtil.dip2px(mContext, 5);
                mp.topMargin = DensityUtil.dip2px(mContext, 10);
                mp.bottomMargin = DensityUtil.dip2px(mContext, 10);
                tv.setLayoutParams(mp);//设置边距

                int padding = DensityUtil.dip2px(mContext, 5);
                tv.setPadding(padding, padding, padding, padding);//设置内边距

                tv.setTextSize(DensityUtil.dip2px(mContext, 12));

                Random random = new Random();
                int red = random.nextInt(150) + 100;
                int green = random.nextInt(150) +100;
                int blue = random.nextInt(150) + 100;

                //设置具有选择器功能的背景
                tv.setBackground(DrawUtils.getSelector(DrawUtils.getDrawable(Color.rgb(red, green, blue), DensityUtil.dip2px(mContext, 5)), DrawUtils.getDrawable(Color.WHITE, DensityUtil.dip2px(mContext, 5))));
                //设置textView是可点击的.如果设置了点击事件，则TextView就是可点击的。

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                flTag.addView(tv);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
