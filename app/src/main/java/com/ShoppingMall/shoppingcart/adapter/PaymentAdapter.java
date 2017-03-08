package com.ShoppingMall.shoppingcart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ShoppingMall.R;
import com.ShoppingMall.home.bean.GoodsBean;
import com.ShoppingMall.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/12 17:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：结算的列表
 */
public class PaymentAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<GoodsBean> listData;

    public PaymentAdapter(Context context, List<GoodsBean> listData) {
        this.mContext = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
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
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_checkout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据位置得到数据
        GoodsBean goodsBean = listData.get(position);
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+goodsBean.getFigure()).into(viewHolder.ivGov);
        viewHolder.tvDescGov.setText(goodsBean.getName());
        viewHolder.tvPriceGov.setText("￥"+goodsBean.getCover_price());

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_gov)
        ImageView ivGov;
        @InjectView(R.id.tv_desc_gov)
        TextView tvDescGov;
        @InjectView(R.id.tv_price_gov)
        TextView tvPriceGov;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
