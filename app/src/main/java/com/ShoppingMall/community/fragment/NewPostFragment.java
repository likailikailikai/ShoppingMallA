package com.ShoppingMall.community.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ShoppingMall.R;
import com.ShoppingMall.base.BaseFragment;
import com.ShoppingMall.community.adatper.NewPostListViewAdapter;
import com.ShoppingMall.community.bean.NewPostBean;
import com.ShoppingMall.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by 情v枫 on 2017/3/4.
 * <p>
 * 作用：新帖Fragment
 */

public class NewPostFragment extends BaseFragment {

    @InjectView(R.id.lv_new_post)
    ListView lvNewPost;
    private NewPostListViewAdapter adapter;

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_news_post, null);
        ButterKnife.inject(this, rootView);
        return rootView;
    }


    /**
     * 1.把数据绑定到控件上的时候，重新该方法
     * 2.联网请求，把得到的数据绑定到视图上
     */
    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils
                .get()
                //联网地址
                .url(Constants.NEW_POST_URL)
                .id(100)//http,
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "联网失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "新帖联网成功==");
                        processData(response);

                    }
                });
    }

    private void processData(String json) {
        NewPostBean bean = JSON.parseObject(json,NewPostBean.class);
        List<NewPostBean.ResultEntity> result = bean.getResult();
        if(result != null && result.size()>0) {
            //设置适配器
            adapter = new NewPostListViewAdapter(mContext,result);
            lvNewPost.setAdapter(adapter);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
