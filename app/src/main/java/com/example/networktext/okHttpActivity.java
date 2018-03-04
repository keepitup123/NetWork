package com.example.networktext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class okHttpActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mbtnSendRequest;
    private TextView mtvResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        mbtnSendRequest = findViewById(R.id.okhttp_sendreques);
        mtvResponse = findViewById(R.id.tv_okhttp);

        mbtnSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okhttp_sendreques){
            sendRequestWithokHttp();
        }
    }

    private void sendRequestWithokHttp() {

        //创建子线程去访问网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建一个OkHttpClient实例
                    Request request = new Request.Builder() //发起HTTP请求，需要创建Request对象，通过builder方式，丰富该对象
                            .url("https://www.baidu.com")//设置url,传入网络地址
                            .build();
                    Response response = client.newCall(request).execute();//调用OkHttpClient的newCall()方法来创建一个Call对象
                                                                            //并调用execut()方法来发送请求并获取服务器返回的数据
                    String responseData = response.body().string(); //获取服务器返回的信息并赋值
                    showResponse(responseData);//传入返回的数据给TextView显示
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程
    }
    //因为不能在子线程更新UI，需要转回到UI线程才能更新UI
    //利用runOnUiThread可以转回UI线程
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mtvResponse.setText(response);
            }
        });
    }
}
