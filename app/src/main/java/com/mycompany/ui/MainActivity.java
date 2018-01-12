package com.mycompany.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mycompany.ui.Information.Information;
import com.mycompany.ui.Logger.Logger;
import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.btnGateway)
    View btnGateway;
    @BindView(R.id.btnLock)
    View btnLock;
    @BindView(R.id.btnAmmeter)
    View btnAmmeter;
    @BindView(R.id.tvGateway)
    TextView tvGateway;
    @BindView(R.id.tvLock)
    TextView tvLock;
    @BindView(R.id.tvAmmeter)
    TextView tvAmmeter;

    private NioClient mclient = null;
    private byte Flag=0;
    private Information information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        information = new Information();
        initView();
    }

    private void initView(){
        mclient = new NioClient(mMessageProcessor,mConnectResultListener);
        mclient.setConnectAddress(new TcpAddress[]{new TcpAddress("192.168.5.148",8000)});
        mclient.connect();
    }

    @OnClick({R.id.btnGateway,R.id.btnAmmeter,R.id.btnLock})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.btnGateway:
                Logger.d("点击获取网关参数按钮");
                String tem = (String) tvGateway.getText();
                if(TextUtils.isEmpty(tvGateway.getText())){
                    Flag=1;
                    mMessageProcessor.send(mclient,"getversion ".getBytes());
//                            information.send("192.168.5.148", 8000, "getversion ", new CallBackInformation() {
//                                @Override
//                                public void onSucces(String message) {
//                                    tvAmmeter.setText(message);
//                                }
//                            });
                }else {
                    Intent intent = new Intent("android.intent.action.GATEWAY");
                    startActivity(intent);
                }
                break;
            case R.id.btnLock:
                if(TextUtils.isEmpty(tvLock.getText())){
//                    mclient.setConnectAddress(new TcpAddress[]{new TcpAddress("192.168.5.148",8000)});
//                    mclient.connect();
                    Flag=2;
                    mMessageProcessor.send(mclient,"getlock ".getBytes());
                }else   {

                }

                break;
            case R.id.btnAmmeter:
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
        public synchronized void onReceiveMessages(final BaseClient mClient, final LinkedList<Message> mQueen) {
            for (int i = 0 ;i< mQueen.size();i++) {
                Message msg = mQueen.get(i);
                final String s = new String(msg.data,msg.offset,msg.length);
                runOnUiThread(new Runnable() {
                    public void run() {
                        switch (Flag)
                        {
                            case 1:
                                tvGateway.setText(s);
                                break;
                            case 2:
                                Logger.e(s);
                                String[] str = s.split(" ");
                                String tem = "";
                                int i=0;
                                while(Character.isDigit(str[i].charAt(0)) && str[i].length()==1 ){
                                    tem = tem+str[i]+" ";
                                    i++;
                                }
                                tvLock.setText(tem);
                                break;
                        }
                    }
                });
            }
        }
    };

}
