package com.example.networktext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;


import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SAXparseActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mbtnSendRequest;
    private TextView mtvResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saxparse);
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
                            .url("http://10.0.2.2/get_data.xml")//设置url,传入网络地址
                            .build();
                    Response response = client.newCall(request).execute();//调用OkHttpClient的newCall()方法来创建一个Call对象
                    //并调用execut()方法来发送请求并获取服务器返回的数据
                    String responseData = response.body().string(); //获取服务器返回的信息并赋值
                    parseXMLWithSAX(responseData);
                    showResponse(responseData);//传入返回的数据给TextView显示
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程
    }

    private void parseXMLWithSAX(String xmlData) {
        SAXParserFactory factory = SAXParserFactory.newInstance(); //获取SAXParserFactory，通过这个实例来获取XMLReader实例
        try {
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();//获取XMLReader实例
            ContentHandler handler = new ContentHandler();//获取ContentHandler实例
            //将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);//将ContentHandler实例传入
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));//调用parse()方法开始执行解析
        }  catch (Exception e) {
            e.printStackTrace();
        }
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
