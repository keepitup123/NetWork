package com.example.networktext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtnHTTP, mbtnokHttp;
    private Button xmlparsewithPull, xmlparsewithSAX;
    private Button JSONObject, GSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbtnokHttp = findViewById(R.id.btn_okhttp);
        mbtnHTTP = findViewById(R.id.btn_http);
        xmlparsewithPull = findViewById(R.id.xml_parse_style1);
        xmlparsewithSAX = findViewById(R.id.xml_parse_style2);
        JSONObject = findViewById(R.id.json_object);
        GSON = findViewById(R.id.json_gson);


        mbtnHTTP.setOnClickListener(this);
        mbtnokHttp.setOnClickListener(this);
        xmlparsewithPull.setOnClickListener(this);
        xmlparsewithSAX.setOnClickListener(this);
        JSONObject.setOnClickListener(this);
        GSON.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_http:
                intent = new Intent(MainActivity.this, HTTPActivity.class);
                break;
            case R.id.btn_okhttp:
                intent = new Intent(MainActivity.this, okHttpActivity.class);
                break;
            case R.id.xml_parse_style1:
                intent = new Intent(MainActivity.this, PullParseActivity.class);
                break;
            case R.id.xml_parse_style2:
                intent = new Intent(MainActivity.this, SAXparseActivity.class);
                break;
            case R.id.json_object:
                intent = new Intent(MainActivity.this,JSONObjectActivity.class);
                break;
            case R.id.json_gson:
                intent = new Intent(MainActivity.this,GSONActivity.class);
                break;
            default:
        }
        startActivity(intent);
    }
}
