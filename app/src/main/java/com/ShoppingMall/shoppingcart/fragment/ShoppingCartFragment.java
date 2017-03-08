package com.ShoppingMall.shoppingcart.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ShoppingMall.MainActivity;
import com.ShoppingMall.R;
import com.ShoppingMall.base.BaseFragment;
import com.ShoppingMall.home.bean.GoodsBean;
import com.ShoppingMall.shoppingcart.activity.PaymentActivtiy;
import com.ShoppingMall.shoppingcart.adapter.ShoppingCartAdapter;
import com.ShoppingMall.shoppingcart.utils.CartStorage;
import com.ShoppingMall.shoppingcart.utils.PayResult;
import com.ShoppingMall.shoppingcart.utils.SignUtils;
import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 情v枫 on 2017/2/22.
 * <p>
 * 购物车 Fragment
 */

public class ShoppingCartFragment extends BaseFragment {

    @InjectView(R.id.tv_shopcart_edit)
    TextView tvShopcartEdit;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @InjectView(R.id.tv_shopcart_total)
    TextView tvShopcartTotal;
    @InjectView(R.id.btn_check_out)
    Button btnCheckOut;
    @InjectView(R.id.ll_check_all)
    LinearLayout llCheckAll;
    @InjectView(R.id.checkbox_delete_all)
    CheckBox checkboxDeleteAll;
    @InjectView(R.id.btn_delete)
    Button btnDelete;
    @InjectView(R.id.btn_collection)
    Button btnCollection;
    @InjectView(R.id.ll_delete)
    LinearLayout llDelete;
    @InjectView(R.id.iv_empty)
    ImageView ivEmpty;
    @InjectView(R.id.tv_empty_cart_tobuy)
    TextView tvEmptyCartTobuy;
    @InjectView(R.id.ll_empty_shopcart)
    LinearLayout llEmptyShopcart;
    private ShoppingCartAdapter adapter;
    List<GoodsBean> list;
    //1、两种状态
    //编辑状态
    private static final int ACTION_EDIT = 1;
    //完成状态
    private static final int ACTION_COMPLETE = 2;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_shopping_cart, null);
        ButterKnife.inject(this, view);
        //设置编辑状态
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        //显示去结算布局
        llCheckAll.setVisibility(View.VISIBLE);

        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.得到状态
                int action = (int) v.getTag();
                //2.根据不同状态做不同的处理
                if (action == ACTION_EDIT) {
                    //切换完成状态
                    showDelete();
                } else {
                    //切换成编辑状态
                    hideDelete();
                }
            }


        });
        return view;
    }

    private void hideDelete() {
        //1.设置编辑
        tvShopcartEdit.setTag(ACTION_EDIT);
        //2.隐藏删除控件
        llDelete.setVisibility(View.GONE);
        //3.显示结算控件
        llCheckAll.setVisibility(View.VISIBLE);
        //4.设置文本为-编辑
        tvShopcartEdit.setText("编辑");
        //5.把所有的数据设置勾选择状态
        if (adapter != null) {
            adapter.checkAll_none(true);
            adapter.checkAll();//校验是否全选
            adapter.showTotalPrice();//显示总价格
        }
    }

    private void showDelete() {
        //1.设置完成
        tvShopcartEdit.setTag(ACTION_COMPLETE);
        //2.显示删除控件
        llDelete.setVisibility(View.VISIBLE);
        //3.隐藏结算控件
        llCheckAll.setVisibility(View.GONE);
        //4.设置文本为-完成
        tvShopcartEdit.setText("完成");
        //5.把所有的数据设置非选择状态
        if (adapter != null) {
            adapter.checkAll_none(false);
            adapter.checkAll();
            adapter.showTotalPrice();
        }
    }

    /**
     * 1.把数据绑定到控件上的时候，重新该方法
     * 2.联网请求，把得到的数据绑定到视图上
     */
    @Override
    public void initData() {
        super.initData();
        //显示数据
        showData();
    }

    private void showData() {
        Log.e("TAG", "购物车数据被初始化了");
        //得到数据
        list = CartStorage.getInstance(mContext).getAllData();
        if (list != null && list.size() > 0) {
            //购物车有数据
            llEmptyShopcart.setVisibility(View.GONE);
            adapter = new ShoppingCartAdapter(mContext, list, tvShopcartTotal, checkboxAll, checkboxDeleteAll);
            //设置RecycleView的适配器
            recyclerview.setAdapter(adapter);
            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            //设置点击事件
            adapter.setOnItemClickListener(new ShoppingCartAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    //设置bean对象状态去反
                    GoodsBean goodsBean = list.get(position);
                    //状态取反
                    goodsBean.setChecked(!goodsBean.isChecked());

                    //刷新-重新绘制该条
                    adapter.notifyItemChanged(position);

                    //刷新价格
                    adapter.showTotalPrice();

                    //校验是否全选
                    adapter.checkAll();
                }
            });
            adapter.checkAll();

        } else {
            //没有数据
            llEmptyShopcart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv_shopcart_edit, R.id.checkbox_all, R.id.btn_check_out, R.id.checkbox_delete_all, R.id.btn_delete, R.id.btn_collection, R.id.tv_empty_cart_tobuy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopcart_edit:
                Toast.makeText(mContext, "编辑", Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_all:
//                Toast.makeText(mContext, "全选", Toast.LENGTH_SHORT).show();
                boolean isChecked = checkboxAll.isChecked();
                //全选和反选
                adapter.checkAll_none(isChecked);
                //显示总价格
                adapter.showTotalPrice();
                break;
            case R.id.btn_check_out:
//                Toast.makeText(mContext, "结算", Toast.LENGTH_SHORT).show();
//                pay();
                Intent intent1 = new Intent(mContext, PaymentActivtiy.class);
                startActivity(intent1);
                break;
            case R.id.checkbox_delete_all:
//                Toast.makeText(mContext, "删除全部", Toast.LENGTH_SHORT).show();
                isChecked = checkboxDeleteAll.isChecked();
                //全选和反全选
                adapter.checkAll_none(isChecked);

                //显示总价格
                adapter.showTotalPrice();
                break;
            case R.id.btn_delete:
//                Toast.makeText(mContext, "删除按钮", Toast.LENGTH_SHORT).show();
                adapter.deleteData();
                adapter.checkAll();
                showEempty();//显示没有数据的默认页面
                break;
            case R.id.btn_collection:
                Toast.makeText(mContext, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_empty_cart_tobuy:
//                Toast.makeText(mContext, "去逛逛", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.putExtra("checkedid",R.id.rb_home);
                startActivity(intent);
                break;
        }
    }

    private void showEempty() {
        if(adapter.getItemCount()==0) {
            llEmptyShopcart .setVisibility(View.VISIBLE);
        }
    }


}
