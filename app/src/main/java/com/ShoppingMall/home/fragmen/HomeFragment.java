package com.ShoppingMall.home.fragmen;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ShoppingMall.R;
import com.ShoppingMall.app.SearchActivity;
import com.ShoppingMall.base.BaseFragment;
import com.ShoppingMall.home.adapter.HomeAdapter;
import com.ShoppingMall.home.bean.HomeBean;
import com.ShoppingMall.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 情v枫 on 2017/2/22.
 * <p>
 * 主页 Fragment
 */

public class HomeFragment extends BaseFragment {
    @InjectView(R.id.tv_search_home)
    TextView tvSearchHome;
    @InjectView(R.id.tv_message_home)
    TextView tvMessageHome;
    @InjectView(R.id.rv_home)
    RecyclerView rvHome;
    @InjectView(R.id.ib_top)
    ImageButton ibTop;
    @InjectView(R.id.ll_main_scan)
    LinearLayout llmainscan;

    private HomeAdapter adapter;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;


    @Override
    public View initView() {
        Log.e("TAG", "主页视图被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        ButterKnife.inject(this, view);
        //设置点击事件
        return view;
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
    public void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(Constants.HOME_URL)

                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","联网失败=="+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","联网成功==");
                        processData(response);
                    }
                });
    }

    /**
     * 1、三种解析方式：fastjson解析数据、gson和手动解析数据
     * 2、社会自适配器
     * @param response
     */
    private void processData(String response) {
        //使用fastjson解析json数据
        HomeBean homeBean = JSON.parseObject(response,HomeBean.class);
        Log.e("TAG", "解析数据成功=="+homeBean.getResult().getHot_info().get(0).getName());

        //设置RecycleView的适配器
        adapter = new HomeAdapter(mContext,homeBean.getResult());
        rvHome.setAdapter(adapter);

        GridLayoutManager manager =new GridLayoutManager(mContext,1);

        //设置布局管理器
        rvHome.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position <= 3) {
                    //按钮隐藏
                    ibTop.setVisibility(View.GONE);
                }else{
                    //按钮显示
                    ibTop.setVisibility(View.VISIBLE);
                }
                return 1;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv_search_home, R.id.tv_message_home, R.id.ib_top,R.id.ll_main_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_home:
//                Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent1);
                break;
            case R.id.tv_message_home:
                Toast.makeText(mContext, "进入消息中心", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_top:
                //回到顶部
                rvHome.scrollToPosition(0);
                break;
            case R.id.ll_main_scan:
//                Toast.makeText(mContext, "扫一扫", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }


}
