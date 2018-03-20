package com.mycompany.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by qkz on 2017/12/12.
 */

public class GatewayFragment extends Fragment {
    @BindView(R.id.srl_list_view)
    SwipeRefreshLayout srlList;
    @BindView(R.id.gateway_list_view)
    RecyclerView listView;
    private List<ListContext> listContextList = new LinkedList<ListContext>();
    private List<ListContext> listContextListReal = new LinkedList<ListContext>();
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
    private ListContextAdapter adapter;
    private NioClient mClient = null;
    final static int GetPara = 1;
    final static int SetPara = 2;
    final static int SavePata = 3;
    static private int Flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gateway, container, false);
        ButterKnife.bind(this,view);
        initNetPara();
        srlList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Flag = GetPara;
                mMessageProcessor.send(mClient,"getpara ".getBytes());
                mClient.setConnectAddress(new TcpAddress[]{new TcpAddress(GatewayIpandPort.ip,GatewayIpandPort.port)});
                mClient.connect();
            }
        });

        adapter = new ListContextAdapter((LinkedList<ListContext>) listContextList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        adapter.setClickItem(new ListContextAdapter.ClickItem() {
             @Override
             public void clickItem(final View view, final int position) {
                 final ListContext listContext = listContextList.get(position);
                 alertDialogUtils.showDialog(listContext.getValueOld(), new CallBackInformation() {
                     @Override
                     public void onSucces(String message) {
                         if (!message.equals(listContext.getValueOld())) {
                             listContextList.set(position, new ListContext(paraDisplay[position], message));
                             listContextListReal.set(position, new ListContext(listContextListReal.get(position).getName(),message,listContextListReal.get(position).getValueNew()));
                             adapter.notifyDataSetChanged();
                             if(!listContextListReal.get(position).getValueNew().equals(listContextListReal.get(position).getValueOld())){
                                 String setMessage = "set";
                                 setMessage = setMessage + " " + listContextListReal.get(position).getName() + " " + listContextListReal.get(position).getValueNew();
                                 Logger.e("setMessage",setMessage);
                                 Flag = SetPara;
                                 mMessageProcessor.send(mClient,setMessage.getBytes());
                                 mClient.setConnectAddress(new TcpAddress[]{new TcpAddress(GatewayIpandPort.ip,GatewayIpandPort.port)});
                                 mClient.connect();
                             }
                         }
                     }

                     @Override
                     public void onFail() {

                     }
                 });
             }
         });
        return view;
    }

    //重新加载toolbar上的布局menu_home

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_gateway, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {
            Flag = SavePata;
            mMessageProcessor.send(mClient,"save ".getBytes());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化网关连接参数
    private void initNetPara(){
        mClient = new NioClient(mMessageProcessor,mConnectResultListener);
        mClient.setConnectAddress(new TcpAddress[]{new TcpAddress(GatewayIpandPort.ip,GatewayIpandPort.port)});
        mClient.connect();
        alertDialogUtils = new AlertDialogUtils(getActivity());
        for(int i=0;i < paraDisplay.length;i++){
            listContextList.add(new ListContext(paraDisplay[i]," "));
            listContextListReal.add(new ListContext(paraReal[i]," "));
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
        public synchronized void onReceiveMessages(final BaseClient client, final LinkedList<Message> mQueen) {
            for (int i = 0 ;i< mQueen.size();i++) {
                Message msg = mQueen.get(i);
                final String s = new String(msg.data,msg.offset,msg.length);
                Logger.e(s);
                if(Flag == GetPara){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            for(int i=0;i < paraDisplay.length;i++){
                                listContextList.set(i, new ListContext(paraDisplay[i], GetPara(s,paraReal[i])));
                                listContextListReal.set(i,new ListContext(paraReal[i],GetPara(s,paraReal[i])));
                                adapter.notifyDataSetChanged();
                            }
                            srlList.setRefreshing(false);
                        }
                    });
                }
            }
        }
    };

//    private void SetPara(){
//        String setMessage = "set";
//        for(int temPosition = 0; temPosition < listContextListReal.size(); temPosition++) {
//            if(!listContextListReal.get(temPosition).getValueNew().equals(listContextListReal.get(temPosition).getValueOld())) {
//                setMessage = setMessage + " " + listContextListReal.get(temPosition).getName() + " " + listContextListReal.get(temPosition).getValueNew();
//            }
//        }
//        Logger.e("setMessage",setMessage);
//        mMessageProcessor.send(mClient,setMessage.getBytes());
//    }

    private String GetPara(String getpara,String para) {
        int i=0;
        String getgrape[] = getpara.split(" ");
        for (String element : getgrape){
            if(para.equals(getgrape[i]))return getgrape[i+1];;
            i++;
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mClient.onClose();
        mClient.disconnect();
    }

}

