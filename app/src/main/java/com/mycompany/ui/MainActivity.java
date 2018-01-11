package com.mycompany.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

public class MainActivity extends AppCompatActivity{

    private NioClient mclient = null;
    private List<ListContext> listContextList =  new LinkedList<ListContext>();
    private ListContextAdapter adapter;
    private byte Flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        initView();
        adapter = new ListContextAdapter((LinkedList<ListContext>) listContextList,MainActivity.this);
        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position ,long id){
                ListContext listContext = listContextList.get(position);
                switch (listContext.getName())
                {
                    case "网关":
                        if(listContextList.get(0).getValue() == null){
                            Flag=1;
                            mMessageProcessor.send(mclient,"getversion ".getBytes());
                        }else {
                            mclient.onClose();
                            Intent intent = new Intent("android.intent.action.GATEWAY");
                            startActivity(intent);
                        }
                        break;
                    case "门锁":
                        Flag=2;
                        mMessageProcessor.send(mclient,"getlock ".getBytes());
                        break;
                }
            }
        });
    }

    private void initView(){
        mclient = new NioClient(mMessageProcessor,mConnectResultListener);
        mclient.setConnectAddress(new TcpAddress[]{new TcpAddress("192.168.5.148",8000)});
        mclient.connect();
        ListContext listContext = new ListContext("网关",null);
        listContextList.add(listContext);
        ListContext lock = new ListContext("门锁",null);
        listContextList.add(lock);
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
                        switch (Flag)
                        {
                            case 1:
                                listContextList.set(0, new ListContext("网关", s));
                                adapter.change(0, new ListContext("网关", s));
                                Log.e(s,"version");
                                break;
                            case 2:
                                listContextList.set(1,new ListContext("门锁",s));
                                adapter.change(1,new ListContext("门锁",s));
                                Log.e(s,"lock");
                                break;
                        }
                    }
                });
            }
        }
    };

}
