package com.ShoppingMall.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ShoppingMall.R;
import com.ShoppingMall.home.adapter.HomeAdapter;
import com.ShoppingMall.home.bean.GoodsBean;

public class QrcodeGenerator extends AppCompatActivity {

    private GoodsBean goodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        getData();
    }

    private void getData() {
        goodsBean = (GoodsBean) getIntent().getSerializableExtra(HomeAdapter.GOODS_BEAN);
        setData();
    }

    private void setData() {
        String s = goodsBean.getCover_price() + goodsBean.getName() + goodsBean.getFigure();

    }
}
