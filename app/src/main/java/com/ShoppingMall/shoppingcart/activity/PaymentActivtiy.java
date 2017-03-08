package com.ShoppingMall.shoppingcart.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ShoppingMall.R;
import com.ShoppingMall.home.bean.GoodsBean;
import com.ShoppingMall.shoppingcart.adapter.PaymentAdapter;
import com.ShoppingMall.shoppingcart.bean.AddressBean;
import com.ShoppingMall.shoppingcart.utils.CartStorage;
import com.ShoppingMall.shoppingcart.utils.PayResult;
import com.ShoppingMall.shoppingcart.utils.SignUtils;
import com.ShoppingMall.view.NoScrollListView;
import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PaymentActivtiy extends AppCompatActivity {

    @InjectView(R.id.tv_left_back)
    TextView tvLeftBack;
    @InjectView(R.id.title_right)
    ImageView titleRight;
    @InjectView(R.id.tvAddresseeName)
    TextView tvAddresseeName;
    @InjectView(R.id.tvSmeAredAddress)
    TextView tvSmeAredAddress;
    @InjectView(R.id.tvAddressNext)
    TextView tvAddressNext;
    @InjectView(R.id.detailAddress)
    TextView detailAddress;
    @InjectView(R.id.addressRelative)
    RelativeLayout addressRelative;
    @InjectView(R.id.tv_paymode)
    TextView tvPaymode;
    @InjectView(R.id.alipay)
    RadioButton alipay;
    @InjectView(R.id.weixing)
    RadioButton weixing;
    @InjectView(R.id.rg_paymode)
    RadioGroup rgPaymode;
    @InjectView(R.id.checkOutListView)
    NoScrollListView checkOutListView;
    @InjectView(R.id.tvCheckOutAllPrice)
    TextView tvCheckOutAllPrice;
    @InjectView(R.id.btnSureCheckOut)
    Button btnSureCheckOut;
    private ArrayList<AddressBean> addressList;
    private List<GoodsBean> listData;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activtiy);
        ButterKnife.inject(this);
        getAddressData();
        showAddressInfo(0);
        setData();
        showTotalPrice();
    }


    private void setData() {
        rgPaymode.check(R.id.alipay);
        listData = CartStorage.getInstance(this).getAllData();

        if (listData != null && listData.size() > 0) {
            adapter = new PaymentAdapter(this, listData);
            checkOutListView.setAdapter(adapter);
        }
    }

    private void showTotalPrice() {
        tvCheckOutAllPrice.setText("合计:" + getTotalPrice());
    }

    private double getTotalPrice() {
        double totalPrice = 0.0;

        if (listData != null && listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {

                GoodsBean goodsBean = listData.get(i);
                if (goodsBean.isChecked()) {
                    //被选中的
                    totalPrice = totalPrice + goodsBean.getNumber() * Double.parseDouble(goodsBean.getCover_price());
                }
            }
        }

        return totalPrice;
    }

    private void getAddressData() {
        addressList = new ArrayList<AddressBean>();

        AddressBean bean = new AddressBean();
        bean.setName("Mr.李先生");
        bean.setSmearedAddress("北京市朝阳区");
        bean.setDetailAddress("故宫博物院  17326991159");
        addressList.add(bean);

        AddressBean bean2 = new AddressBean();
        bean2.setName("Mr.李先生");
        bean2.setSmearedAddress("北京市朝阳区");
        bean2.setDetailAddress("北京朝阳区故宫博物院乾清宫  17326991159");
        addressList.add(bean2);
    }

    private void showAddressInfo(int index) {
        AddressBean bean = addressList.get(index);
        tvAddresseeName.setText(bean.getName());
        tvSmeAredAddress.setText(bean.getSmearedAddress());
        detailAddress.setText(bean.getDetailAddress());

    }

    @OnClick({R.id.tv_left_back, R.id.tvAddressNext, R.id.btnSureCheckOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left_back:
                finish();
                break;
            case R.id.tvAddressNext:
                // 修改收货地址
                updateAddress();
                break;
            case R.id.btnSureCheckOut:
                int checkId = rgPaymode.getCheckedRadioButtonId();
                if (checkId == R.id.alipay) {
                    //支付
                    pay(null);
                } else if (checkId == R.id.weixing) {
                    //微信支付
                    Toast.makeText(this, "微信暂未完成", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private static int REQUESTCODE = 1;          //跳转请求码

    //修改地址
    private void updateAddress() {
        Intent intent = new Intent(PaymentActivtiy.this, UpdateAddressActivity.class);
        intent.putExtra("AddressLiist", addressList);
        startActivityForResult(intent,REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int addressIndex = bundle.getInt("addressIndex");
            showAddressInfo(addressIndex);
        }
    }

    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANDl5XiZplckwpJ/\n" +
            "EVejypog8kwjqVPAWQ0tGYWG/E4g1ccF+KjlICSWlkPiJY6JTpVwQ9xbfA12HVEZ\n" +
            "6hH4k9GIIJI7+H5/dDOFdCRyJUcXGx/6cTHiK1oe3BxJ/+vtYRCmqX6FaiChXSmr\n" +
            "nero6+QtMhElWVlxXsQoll6rcQg5AgMBAAECgYBrW4tIJKcBKCo7AcTW5p2MApyj\n" +
            "lrxCB5t8kw4HuqzWNjfHtgDqQ/717tJBButISraxRGYLzJR/kpFUid3q6HaGjdcs\n" +
            "8IHPTAtTaOMVrp3MVizoJgd2JQ5ATO0loMVqAzqCftmr/UsKYzt/r/ghBQUq73cQ\n" +
            "KHPLpTq98Z+GONvxJQJBAPKup+v5NAJARPfl4s6j/j/JJ8B4E5VXm0CFjR2dCLk6\n" +
            "e15lQ1U2Sc7XJHm5gJUmxAVMlVhohDLFr+chwx0aAisCQQDcXJ6mUhB5TnpMWljD\n" +
            "Kn4CJA0bK5vQhZmOIcKrPfb/q90qzgqJTEckz1CPY+Erx/vbiiAxVNuXR8ADmtco\n" +
            "cYErAkA+1BbnUco0Nv1kDKEujGh7jRF8k9nGFTs9dhX1r70db1WXN7I58mcjZLGt\n" +
            "zefLNZEUKiKUE+L93g5PUX9ZHm2nAkEAnOxgFjTuglQ+h0z3VNRXs5KUurqNVSsu\n" +
            "tKaaCHQyBtQOxcLAghGNwEraJaKM4S6izi5IypiRX+O6e1ayzZw2zQJBAIMDTt5g\n" +
            "j1lZEBrLIxo2mRrTJKLQCLuW77CrMJtN6+79G39X9DBeao7dGP2agyVE7SYTJiV+\n" +
            "E0hkilFDq3xItew=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQ5eV4maZXJMKSfxFXo8qaIPJMI6lTwFkNLRmFhvxOINXHBfio5SAklpZD4iWOiU6VcEPcW3wNdh1RGeoR+JPRiCCSO/h+f3QzhXQkciVHFxsf+nEx4itaHtwcSf/r7WEQpql+hWogoV0pq53q6OvkLTIRJVlZcV7EKJZeq3EIOQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PaymentActivtiy.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PaymentActivtiy.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PaymentActivtiy.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(PaymentActivtiy.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
//                            finish();
                        }
                    }).show();
            return;
        }
        //生成订单信息-在客户端或者服务器
        String orderInfo = getOrderInfo("硅谷内裤", "好棒好棒的内裤", getTotalPrice()+"");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) PaymentActivtiy.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
