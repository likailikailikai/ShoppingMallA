package com.ShoppingMall.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ShoppingMall.app.SearchActivity;
import com.ShoppingMall.home.bean.TypeListBean;

import java.util.List;

/**
 * Created by 情v枫 on 2017/3/8.
 * <p>
 * 作用：
 */

public class SearchAdapter extends BaseAdapter {
    public SearchAdapter(SearchActivity searchActivity, List<TypeListBean.ResultBean> items) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
