package com.mycompany.ui.Information;

import com.mycompany.ui.Logger.Logger;
import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

import java.util.LinkedList;

/**
 * Created by qkz on 2018/1/11.
 */

public class Information {

    private NioClient mclient = null;
    private String sendMessage;
    private String receiveMessage;
    private CallBackInformation callBackInformation;

    private void connect(String ip,int port) {
        mclient = new NioClient(mMessageProcessor, mConnectResultListener);
        mclient.setConnectAddress(new TcpAddress[]{new TcpAddress(ip, port)});
        mclient.connect();
    }

    private IConnectListener mConnectResultListener = new IConnectListener() {
        @Override
        public void onConnectionSuccess() {
            Logger.e("连接成功");
        }

        @Override
        public void onConnectionFailed() {
            Logger.e("连接失败");
        }
    };

    public void send(String ip,int port,String sendMessage, CallBackInformation callBackInformation){

        connect(ip,port);

        this.callBackInformation = callBackInformation;
        this.sendMessage = sendMessage;
        mMessageProcessor.send(mclient,sendMessage.getBytes());
    }


    private BaseMessageProcessor mMessageProcessor =new BaseMessageProcessor() {
        @Override
        public synchronized void onReceiveMessages(BaseClient mClient, final LinkedList<Message> mQueen) {
            for (int i = 0 ;i< mQueen.size();i++) {
                Message msg = mQueen.get(i);
                receiveMessage = new String(msg.data,msg.offset,msg.length);
                Logger.e(receiveMessage);
                callBackInformation.onSucces(receiveMessage);
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        switch (Flag)
//                        {
//                            case 1:
//                                listContextList.set(0, new ListContext("网关", s));
//                                adapter.change(0, new ListContext("网关", s));
//                                Log.e(s,"version");
//                                break;
//                            case 2:
//                                listContextList.set(1,new ListContext("门锁",s));
//                                adapter.change(1,new ListContext("门锁",s));
//                                Log.e(s,"lock");
//                                break;
//                        }
//                    }
//                });
            }
        }
    };

}
