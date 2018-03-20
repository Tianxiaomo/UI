package com.mycompany.ui.Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.mycompany.ui.NatifyCallBack;
import com.mycompany.ui.R;

import java.util.Date;

/**
 * Created by qkz on 2018/3/7.
 */

public class AllDialog {
    private AlertDialog addcardDialog;
    private ProgressDialog waitingDialog;
    private Activity activity;
    private BleDevice bleDevice;
    static final byte addPassword = 1;
    static final byte Timing = 5;
    static final byte Electric = 7;
    static final byte Rssi = (byte) 0xA1;


    public AllDialog(Activity activity, BleDevice bleDevice){
        addcardDialog = new AlertDialog.Builder(activity).create();
        this.activity = activity;
        this.bleDevice = bleDevice;
    }

    public void delectpwDialog() {
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_add_card);
//        TextView tvCardID = window.findViewById(R.id.card_id);
//        tvCardID.setText(activity.getString(R.string.password));
        TextView tvTitle = (TextView)window.findViewById(R.id.layout_dialog_add_card_title);
        tvTitle.setText(activity.getString(R.string.delect_password));
        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
    }
    public void addpwDialog() {
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_add_card);
//        TextView tvCardID = window.findViewById(R.id.card_id);
//        tvCardID.setText(activity.getString(R.string.password));
        TextView tvTitle = (TextView)window.findViewById(R.id.layout_dialog_add_card_title);
        tvTitle.setText(activity.getString(R.string.add_password));

        final EditText etLock = window.findViewById(R.id.layout_dialog_add_card_ed_lock);
//        final EditText etData = window.findViewById(R.id.layout_dialog_add_card_ed_card);

        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lock = Integer.parseInt(etLock.getText().toString());
                if(lock < 255) {
                    writeToBle((byte)lock,(byte)1);
                    addcardDialog.dismiss();
                }else{
                    Toast.makeText(activity,"请输入正确锁号",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void delectcardDialog() {
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_add_card);
        TextView tvTitle = (TextView)window.findViewById(R.id.layout_dialog_add_card_title);
        tvTitle.setText(activity.getString(R.string.delect_card));
        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
    }
    public void addcardDialog(){
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);

        window.setContentView(R.layout.dialog_add_card);
        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
    }

    public void dialog(int position) {
        byte cmd = 0;
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_add_card);
        TextView tvTitle = (TextView)window.findViewById(R.id.layout_dialog_add_card_title);
        switch (position){
            case 0:
                tvTitle.setText(activity.getString(R.string.add_password));
                cmd = addPassword;
                break;
            case 1:
                tvTitle.setText(activity.getString(R.string.timing));
                cmd = Timing;
                break;
            case 2:
                tvTitle.setText(activity.getString(R.string.electric));
                cmd = Electric;
                break;
            case 3:
                tvTitle.setText(activity.getString(R.string.rssi));
                cmd = Rssi;
                break;
            default:
                break;
        }
        final EditText etLock = window.findViewById(R.id.layout_dialog_add_card_ed_lock);

        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        final byte finalCmd = cmd;
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lock = Integer.parseInt(etLock.getText().toString());
                if(lock < 255) {
                    writeToBle((byte)lock, finalCmd);
                    addcardDialog.dismiss();
                }else{
                    Toast.makeText(activity,"请输入正确锁号",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showWaitingDialog() {
        waitingDialog = new ProgressDialog(activity);
        waitingDialog.setTitle("发成功了，给我数据");
        waitingDialog.setMessage("等待中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        waitingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    waitingDialog.dismiss();
                }
                return false;
            }
        });
    }

    private void writeToBle(byte lock,byte cmd){
        Date dataNow = new Date();
        Date date = new Date(110,1,1,0,0,0);
        byte hex[] = intToHex((dataNow.getTime() - date.getTime())/1000);
        hex[4] = lock;
        hex[5] = cmd;
        BleManager.getInstance().write(
            bleDevice,
                "0000abf0-0000-1000-8000-00805f9b34fb",
                "0000abf3-0000-1000-8000-00805f9b34fb",
                hex,
            false,
            new BleWriteCallback() {

                @Override
                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"写成功",Toast.LENGTH_SHORT).show();
                            showWaitingDialog();
                            natifyFromBle(new NatifyCallBack() {
                                @Override
                                public void reciveData(byte[] message) {
                                    waitingDialog.dismiss();
                                    String str = new String(message);
                                    Toast.makeText(activity,str,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }

                @Override
                public void onWriteFailure(final BleException exception) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"写失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }

    private void natifyFromBle(final NatifyCallBack natifyCallBack){
        BleManager.getInstance().notify(
                bleDevice,
                "0000abf0-0000-1000-8000-00805f9b34fb",
                "0000abf4-0000-1000-8000-00805f9b34fb",
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        // 打开通知操作成功
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        // 打开通知操作失败
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        natifyCallBack.reciveData(data);
                        BleManager.getInstance().stopNotify(bleDevice,
                                "0000abf0-0000-1000-8000-00805f9b34fb",
                                "0000abf2-0000-1000-8000-00805f9b34fb");
                    }
                });
    }

    private byte[] intToHex(long value){
        byte[] hex = new byte[6];
        hex[0] = (byte)(value%0xFF);
        hex[1] = (byte)(value%0xFFFF/0xFF);
        hex[2] = (byte)(value%0xFFFFFF/0xFFFF);
        hex[3] = (byte)(value/0xFFFFFF);
        return hex;
    }

}
