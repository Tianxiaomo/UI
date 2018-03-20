package com.mycompany.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.mycompany.ui.Logger.Logger;
import com.mycompany.ui.adapter.DeviceAdapter;
import com.mycompany.ui.operation.LockFragment;
import com.open.net.client.impl.tcp.nio.NioClient;
import com.open.net.client.structures.BaseClient;
import com.open.net.client.structures.BaseMessageProcessor;
import com.open.net.client.structures.IConnectListener;
import com.open.net.client.structures.TcpAddress;
import com.open.net.client.structures.message.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment {
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

    RecyclerView rvDialogBle;

    private String TAG = getTag();
    private String KEY = "Bledevice";
    private NioClient mclient = null;
    private byte Flag=0;
    AlertDialog bleDialog;
    DeviceAdapter deviceAdapter;
    private ProgressDialog progressDialog;

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    public MainFragment() {
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bleDialog = new AlertDialog.Builder(getContext()).create();
        progressDialog = new ProgressDialog(getContext());
        deviceAdapter = new DeviceAdapter();

        BleManager.getInstance().init(getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(7)
                .setOperateTimeout(5000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        mclient = new NioClient(mMessageProcessor,mConnectResultListener);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mclient.onClose();
        mclient.disconnect();
    }


    private void initView(){
        mclient.setConnectAddress(new TcpAddress[]{new TcpAddress(GatewayIpandPort.ip,GatewayIpandPort.port)});
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
                    initView();
                    mMessageProcessor.send(mclient,"getversion ".getBytes());
                }else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main,new GatewayFragment())
                             .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.btnLock:
                bleDialog();
                break;
            case R.id.btnAmmeter:
                Date dataNow = new Date();
                Date date = new Date(2010,1,1,0,0,0);
                long second = dataNow.getTime() - date.getTime();
                Toast.makeText(getContext(),String.valueOf(second),Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void bleDialog() {
        bleDialog.show();
        Window window = bleDialog.getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = bleDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度

        bleDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_ble);

        rvDialogBle = window.findViewById(R.id.dialog_ble_rv);
        rvDialogBleInit();

        window.findViewById(R.id.dialog_ble_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleDialog.dismiss();
            }
        });
        TextView tvDialogBleConfirm = window.findViewById(R.id.dialog_ble_confirm);
        tvDialogBleConfirm.setText("重新扫描");
        window.findViewById(R.id.dialog_ble_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkPermissions();
            }
        });

        checkPermissions();
    }

    private void rvDialogBleInit(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvDialogBle.setLayoutManager(linearLayoutManager);
        rvDialogBle.setHasFixedSize(true);
        rvDialogBle.setAdapter(deviceAdapter);
        deviceAdapter.setClickItem(new DeviceAdapter.ClickItem() {
            @Override
            public void clickItem(final View view, final int position) {
                BleDevice bleDevice = deviceAdapter.getScanDevice(position);
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }
        });
    }


    //ble 的扫描，权限等
    private void setScanRule() {
        Logger.e(TAG,"设置扫描规则");
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Logger.e(TAG, "onScanStarted");
                deviceAdapter.clearScanDevice();
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.e(TAG, "scanning");
                if(!TextUtils.isEmpty(bleDevice.getName())) {
                    String temp = bleDevice.getName();
                    String tempa = bleDevice.getDevice().getName();
                    deviceAdapter.addDevice(bleDevice);
                    deviceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Logger.e(TAG, "scan finish");
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleException exception) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                deviceAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
                setMtu(bleDevice, 512);

                bleDialog.dismiss();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_main,LockFragment.newInstance(bleDevice))
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                if (isActiveDisConnected) {
                    Toast.makeText(getContext(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Logger.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Logger.e(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Logger.e(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    private void checkPermissions() {
        Logger.e(TAG,"check permission");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Logger.e(TAG,"blue is close");
            Toast.makeText(getContext(), getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            BleManager.getInstance().enableBluetooth();

//            OpenBleDialog openBleDialog = new OpenBleDialog(getActivity());
//            openBleDialog.show(new AlerDialogCallBack() {
//                @Override
//                public void success() {
//                    BleManager.getInstance().enableBluetooth();
//                }
//
//                @Override
//                public void fail() {
//
//                }
//            });

            return;
        }
        Logger.e(TAG,"ble is open");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        Logger.e(TAG,"permission grant ");
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logger.e(TAG,"点击了取消");
//                                            dismiss();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logger.e(TAG,"跳转设置打开GPS");
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        Logger.e(TAG,"检查GPS是否打开");
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        }
    }

    //TCP 连接的监听和数据传输接口
    private IConnectListener mConnectResultListener = new IConnectListener() {
        @Override
        public void onConnectionSuccess() {
            Logger.e("TCP","成功");
        }

        @Override
        public void onConnectionFailed() {
            Logger.e("TCP","失败");
        }
    };

    private BaseMessageProcessor mMessageProcessor =new BaseMessageProcessor() {
        @Override
        public synchronized void onReceiveMessages(final BaseClient mClient, final LinkedList<Message> mQueen) {
            for (int i = 0 ;i< mQueen.size();i++) {
                Message msg = mQueen.get(i);
                final String s = new String(msg.data,msg.offset,msg.length);
                Logger.e("TCP",s);
                getActivity().runOnUiThread(new Runnable() {
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
            mclient.onClose();
            mclient.disconnect();
        }
    };
}
