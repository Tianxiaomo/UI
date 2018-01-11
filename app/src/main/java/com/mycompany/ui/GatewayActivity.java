package com.mycompany.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by qkz on 2017/12/12.
 */

public class GatewayActivity extends Activity {

    private List<ListContext> listContextList = new LinkedList<ListContext>();
    private String[] paraDisplay = {
    "心跳服务器地址",
    "心跳端口",
    "心跳号",
    "心跳间隔",
    "本地服务器端口号",
    "网关区号",
    "433功率",
    "升级地址",
    "MQTT域名",
    "MQTT地址",
    "MQTT端口号"
    };
    private String[] paraReal = {
    "serip",
    "serport",
    "hbdat",
    "hbtime",
    "port",
    "wlnum",
    "si4438pa",
    "updateurl",
    "mqdomain",
    "mqip",
    "mqport",
    "password",
    "passwordenable",
    "mqkeepalive"
    };

//    private GetParaSplit getParaSplit;
    private ListContextAdapter listContextAdapter;
    ListContextAdapter adapter;
    private NioClient mclient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gateway);
        initView();
        //mMessageProcessor.send(mclient,"getpara".getBytes());
        adapter = new ListContextAdapter((LinkedList<ListContext>) listContextList,GatewayActivity.this);
        final ListView listView = findViewById(R.id.gateway_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position , long id){
                ListContext listContext = listContextList.get(position);
                switch (listContext.getName())
                {
                    case "心跳服务器地址":
                        mMessageProcessor.send(mclient,"getpara ".getBytes());
                        break;
//                    case para[1]:
//
//                        break;
                }
            }
        });
    }

    private void initView(){
        mclient = new NioClient(mMessageProcessor,mConnectResultListener);
        mclient.setConnectAddress(new TcpAddress[]{new TcpAddress("192.168.5.148",8000)});
        mclient.connect();
        for(int i=0;i < paraDisplay.length;i++){
        ListContext listContext = new ListContext(paraDisplay[i],"1111111111111");
        listContextList.add(listContext);
         }
    }

    private IConnectListener mConnectResultListener = new IConnectListener() {
        @Override
        public void onConnectionSuccess() {

        }

        @Override
        public void onConnectionFailed() {

        }
    };

    private BaseMessageProcessor mMessageProcessor =new BaseMessageProcessor() {

        @Override
        public synchronized void onReceiveMessages(BaseClient mClient, final LinkedList<Message> mQueen) {
            for (int i = 0 ;i< mQueen.size();i++) {
                Message msg = mQueen.get(i);
                final String s = new String(msg.data,msg.offset,msg.length);
                runOnUiThread(new Runnable() {
                    public void run() {
                        GetParaSplit getParaSplit = new GetParaSplit(s);
                        for(int i=0;i < paraDisplay.length;i++){
                            listContextList.set(i, new ListContext(paraDisplay[i], "aaaaaaa"));
                            String tem=paraReal[i];
                            String tremp = GetPara(s,tem);
                            adapter.change(i, new ListContext(paraDisplay[i],tremp ));
                        }
                    }
                });
            }
        }
    };

    public String GetPara(String getpara,String para) {
        int i=0;
        String getgrape[] = getpara.split(" ");
        for (String element : getgrape){
            if(para.equals(getgrape[i]))return getgrape[i+1];;
            i++;
        }
        return null;
    }
}

