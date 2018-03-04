package com.example.networktext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mbtnSendRequest;
    private TextView mtvResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        mbtnSendRequest = findViewById(R.id.http_sendreques);
        mtvResponse = findViewById(R.id.tv_http);

        mbtnSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.http_sendreques){
            sendRequestWithHttpURLConnection();
        }
    }

    private void sendRequestWithHttpURLConnection() {

        //创建子线程去访问网络
        new Thread(new Runnable() {
            @Override                  //通过HttpURLConnection方式发送HTTP请求，HttpClient 6.0后弃用了
            public void run() {   //创建HttpURLConnection对象和BufferedReader对象  并初始化
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {             //通过new一个URL对象，并传入目标的网络地址，然后调用openConnection()方法来获取HttpURLConnection实例
                    URL url = new URL("https://www.baidu.com");//创建一个URL实例  传入网络地址
                    connection = (HttpURLConnection) url.openConnection();//获取HttpURLConnection实例
                    connection.setRequestMethod("GET");//设置为获取数据模式
                    connection.setConnectTimeout(8000);//设置连接超时时间
                    connection.setReadTimeout(8000);//设置读取超时时间
                    InputStream in = connection.getInputStream();//调用getInputStream（）方法获取服务器返回的输入流

                    //开始对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));// 传入输入流对象实例化BufferedReader
                    StringBuilder response = new StringBuilder();//StringBuilder拼接数据
                    String line;
                    while((line = reader.readLine() ) != null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                }  catch (IOException e) {
                    e.printStackTrace();
                }finally {          //操作完毕后关闭BufferedReader
                    if (reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }       //最后关闭HTTP连接
                    if (connection != null){
                        connection.disconnect();
                    }
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
