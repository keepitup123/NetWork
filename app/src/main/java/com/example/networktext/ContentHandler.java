package com.example.networktext;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**要重写父类的5个方法
 * Created by zhu on 2018/3/1.
 */

public class ContentHandler extends DefaultHandler {

    private String nodeName;

    private StringBuilder id,name,version;


    //在开始解析XML时调用
    @Override
    public void startDocument() throws SAXException {
        id = new StringBuilder(); //实例化
        name = new StringBuilder();
        version = new StringBuilder();
    }

    //在开始解析某个节点时调用
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
       //记录当前节点名
        nodeName = localName;
    }

    //在获取节点中内容时调用，可能会被多次调用，一些换行符也会解析，需要注意
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
       //根据当前的节点名判断将内容添加到哪一个StringBuilder对象中
        if ("id".equals(nodeName)){
            id.append(ch,start,length);
        }else if ("name".equals(nodeName)){
            name.append(ch,start,length);
        }else if ("version".equals(nodeName)){
            version.append(ch,start,length);
        }
    }

    //在完成解析某个节点时调用
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("app".equals(localName)){
            Log.d("ContentHandler", "id is "+id.toString().trim());
            Log.d("ContentHandler", "name is "+name.toString().trim());
            Log.d("ContentHandler", "version is "+version.toString().trim());
            //最后要将StringBuilder清空掉
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    //在完成整个XML解析时调用
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
