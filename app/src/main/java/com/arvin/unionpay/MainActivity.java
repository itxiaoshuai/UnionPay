package com.arvin.unionpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        url = "http://101.231.204.84:8091/sim/getacptn";//银联提供的支付测试接口
    }

    public void unionPay(View v){
        //第三方支付：支付四部曲
        //1.提交信息到服务器
        StringRequest request = new StringRequest(url, this, this);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        //2.解析获取“支付串码”
        //3.调用第三方支付SDK的支付方法，传入“支付串码”
        //4.处理支付结果
    }

    @Override
    public void onResponse(String s) {
        //2.解析获取交易流水号
        String tranNum = s;
        showLog(s);
        //3.调用第三方支付SDK的支付方法，传入“支付串码”
        /**
         * tranNum:交易流水号
         * mode："00"启动银联正式环境 ,"01"连接银联测试环境（可以使用测试账号，测试账号参阅文档）
         */
        String mode = "01";
        UPPayAssistEx.startPayByJAR(MainActivity.this, PayActivity.class, null, null, tranNum, mode);

    }

    //4.处理支付结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String msg = null;
        /** 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消*/
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        showLog("error="+volleyError.getMessage());
    }

    private void showLog(String msg){
        Log.e("result", "" + msg);
    }
}
