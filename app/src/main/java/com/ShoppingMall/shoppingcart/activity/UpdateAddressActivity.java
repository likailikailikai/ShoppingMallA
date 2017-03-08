package com.ShoppingMall.shoppingcart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ShoppingMall.R;
import com.ShoppingMall.shoppingcart.adapter.UpdateAddressAdapter;
import com.ShoppingMall.shoppingcart.bean.AddressBean;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UpdateAddressActivity extends AppCompatActivity {

    @InjectView(R.id.title_left)
    TextView titleLeft;
    @InjectView(R.id.title_center)
    TextView titleCenter;
    @InjectView(R.id.title_right)
    ImageView titleRight;
    @InjectView(R.id.updateAddressListView)
    ListView updateAddressListView;

    private ArrayList<AddressBean> addressList;
    private UpdateAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_address);
        ButterKnife.inject(this);
        getData();
        init();
    }

    private void getData() {
        addressList = (ArrayList<AddressBean>) getIntent().getSerializableExtra("AddressLiist");
    }

    @OnClick(R.id.title_left)
    public void onClick() {
        finish();
    }


    //初始化数据
    private void init() {

        if (addressList != null && addressList.size() >0) {
            adapter = new UpdateAddressAdapter(this, addressList);
            updateAddressListView.setAdapter(adapter);
            updateAddressListView.setOnItemClickListener(new ListViewItemClickListener());
        }
    }

    //ListView条目点击监听器
    private final class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
            Intent intent = new Intent();
            intent.putExtra("addressIndex", position);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
