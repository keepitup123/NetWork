package com.example.networktext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PullParseActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mbtnSendRequest;
    private TextView mtvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_parse);
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
                            .url("http://10.0.2.2/get_data.xml")//访问的服务器地址是电脑本机,对于虚拟机本机IP地址就是  10.0.0.2
                            .build();
                    Response response = client.newCall(request).execute();//调用OkHttpClient的newCall()方法来创建一个Call对象
                    //并调用execut()方法来发送请求并获取服务器返回的数据
                    String responseData = response.body().string(); //获取服务器返回的信息并赋值
                    parseXMLWithPull(responseData);//将返回的数据对象传入进行解析
                    showResponse(responseData);//传入返回的数据给TextView显示
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//获取XmlPullParserFactory实例，借助这个实例来获取XmlPullParser对象
            XmlPullParser xmlPullParser = factory.newPullParser();//获取XmlPullParser对象
            xmlPullParser.setInput(new StringReader(xmlData));//调用XmlPullParser的setInput()方法将服务器返回的XML数据传入，开始解析
            int eventType = xmlPullParser.getEventType();//获取当前解析事件
            String id = ""; //初始化变量
            String name = "";
            String version = "";
            while(eventType != XmlPullParser.END_DOCUMENT){  //通过循环不断进行解析,解析事件等于XmlPullParser.END_DOCUMENT则说明解析完成，
                                                                // 否则未解析完成，继续解析，当解析完成，调用next()方法获取下一个解析事件
            String nodeName = xmlPullParser.getName();//获取当前节点名
            switch (eventType){
                //开始解析某个节点
                case XmlPullParser.START_TAG:{   //如果解析标志位开始，则开始解析
                    if ("id".equals(nodeName)){    //判断节点名是否等于 id   name   version 如果等于就调用nextText（)方法获取节点的具体内容
                        id = xmlPullParser.nextText();
                    }else if("name".equals(nodeName)){
                        name = xmlPullParser.nextText();
                    }else if ("version".equals(nodeName)){
                        version = xmlPullParser.nextText();
                    }
                    break;
                }
                //完成某个节点的解析
                case XmlPullParser.END_TAG:{   //当标志为END_TAG时说明一个app节点已经解析完，就打印出它的具体内容
                    if ("app".equals(nodeName)){
                        Log.d("---XML---Pull", "id is"+id);
                        Log.d("---XML---Pull", "name is"+name);
                        Log.d("---XML---Pull", "version is"+version);
                    }
                    break;
                }
                  default:
            }
            eventType = xmlPullParser.next();//获取下一个解析事件
            }
        }catch (Exception e) {
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
