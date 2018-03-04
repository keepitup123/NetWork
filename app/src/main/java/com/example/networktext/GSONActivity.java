package com.example.networktext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GSONActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mbtnSendRequest;
    private TextView mtvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        mbtnSendRequest = findViewById(R.id.okhttp_sendreques);
        mtvResponse = findViewById(R.id.tv_okhttp);

        mbtnSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okhttp_sendreques) {
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
                            .url("http://10.0.2.2/get_data.json")//设置url,传入网络地址
                            .build();
                    Response response = client.newCall(request).execute();//调用OkHttpClient的newCall()方法来创建一个Call对象
                    //并调用execut()方法来发送请求并获取服务器返回的数据
                    String responseData = response.body().string(); //获取服务器返回的信息并赋值给responseData，作为下面的参数传入
//                    parseJSONWithJSONObject(responseData);//使用JSONObject方式解析JSON数据
                    parseJSONWithGSON(responseData);//使用GSON方式解析JSON数据
                    showResponse(responseData);//传入返回的数据给TextView显示
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();//启动子线程
    }

    /*
    首先要创建一个persion类，并加入id name version 的属性 同时创建getter和setter方法
    */
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();//创建一个GSON对象
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());//解析一个json数组需要借助TypeToken将期望解析成的数据类型
        // 传入到fromJson()方法中
        //遍历数组将解析完成的数据打印出来
        for (App app : appList) {
            Log.d("GSONActivity", "id is  " + app.getId());
            Log.d("GSONActivity", "name is  " + app.getName());
            Log.d("GSONActivity", "verson  " + app.getVersion());
        }
    }

/*    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData); //我们再服务器中定义的是一个数组，所以用一个JSONArray对象来接收服务器返回的数据
            for (int i = 0 ; i <jsonArray.length() ; i++){  //遍历数组，取出的每一个元素都是JSONObject对象，每个JSONObject对象都包含id  name version 数据
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");  //通过toString（）方法取出数据
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("---JSONObject---", "id is "+id);
                Log.d("---JSONObject---", "name is "+name);
                Log.d("---JSONObject---", "version is "+version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

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
