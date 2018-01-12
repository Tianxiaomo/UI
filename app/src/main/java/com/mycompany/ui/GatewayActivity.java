package com.mycompany.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mycompany.ui.Information.CallBackInformation;
import com.mycompany.ui.Logger.Logger;
import com.mycompany.ui.Utils.AlertDialogUtils;
import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qkz on 2017/12/12.
 */

public class GatewayActivity extends AppCompatActivity {
    @BindView(R.id.btn_gateway_set)
    View btnGatewaySet;
    @BindView(R.id.btn_gateway_get)
    View btnGatewayGet;

    private List<ListContext> listContextList = new LinkedList<ListContext>();
    private final String[] paraDisplay = {
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

    private AlertDialogUtils alertDialogUtils;
    private ListContextAdapter listContextAdapter;
    ListContextAdapter adapter;
    private NioClient mClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gateway);
        ButterKnife.bind(this);
        initView();

        adapter = new ListContextAdapter((LinkedList<ListContext>) listContextList,GatewayActivity.this);

        final ListView listView = findViewById(R.id.gateway_list_view);
        listView.setAdapter(adapter);
//        listView.set
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position , long id){
                final ListContext listContext = listContextList.get(position);
//                switch (position)
//                {
//                    case 0:
                        alertDialogUtils.showDialog(listContext.getValue(), new CallBackInformation() {
                            @Override
                            public void onSucces(String message) {
                                if(!message.equals(listContext.getValue())) {
                                    listContextList.set(position, new ListContext(paraDisplay[position], message));
                                    adapter.changeSelected(position);
                                }
                            }
                        });
                       // listContextList.set(position, new ListContext(paraDisplay[position], ""));
//                        break;
//                    case 1:
//                        alertDialogUtils.showDialog(listContext.getValue(), new CallBackInformation() {
//                            @Override
//                            public void onSucces(String message) {
//                                listContextList.set(position,new ListContext(paraDisplay[position],message));
//                                adapter.changeSelected(position);
//                            }
//                        });
//                        break;
//                }
            }
        });
    }

    private void initView(){
        mClient = new NioClient(new BaseMessageProcessor() {
            @Override
            public synchronized void onReceiveMessages(BaseClient mClient, final LinkedList<Message> mQueen) {
                for (int i = 0 ;i< mQueen.size();i++) {
                    Message msg = mQueen.get(i);
                    final String s = new String(msg.data,msg.offset,msg.length);
                    Logger.e(s);
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            GetParaSplit getParaSplit = new GetParaSplit(s);
                            for(int i=0;i < paraDisplay.length;i++){
                                listContextList.set(i, new ListContext(paraDisplay[i], GetPara(s,paraReal[i])));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        } ,mConnectResultListener);

        mClient.setConnectAddress(new TcpAddress[]{new TcpAddress("192.168.5.148",8000)});
        mClient.connect();

        alertDialogUtils = new AlertDialogUtils(GatewayActivity.this);

        for(int i=0;i < paraDisplay.length;i++){
        ListContext listContext = new ListContext(paraDisplay[i]," ");
        listContextList.add(listContext);
         }
    }

    @OnClick({R.id.btn_gateway_set,R.id.btn_gateway_get})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.btn_gateway_set:

                break;
            case R.id.btn_gateway_get:
         //       mMessageProcessor.send(mClient,"getpara ".getBytes());
                mClient.onSendMessage("getpara ".getBytes(),0,"getpara ".length());
                break;
            default:
                break;
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
                Logger.e(s);
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

