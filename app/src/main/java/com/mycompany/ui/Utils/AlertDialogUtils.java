package com.mycompany.ui.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.mycompany.ui.Information.CallBackInformation;

/**
 * Created by qkz on 2018/1/11.
 */

public class AlertDialogUtils {

    private Context context;
    private CallBackInformation callBackInformation;

    public AlertDialogUtils(Context context){
        this.context=context;
    }

    public void showDialog(String string, final CallBackInformation callBackInformation) {
        this.callBackInformation = callBackInformation;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入：");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        final EditText editText = new EditText(context);
        editText.setText(string);
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String string = String.valueOf(editText.getText());
                    Toast.makeText(context,"点击了确定按钮"+ string,Toast.LENGTH_SHORT).show();
                    callBackInformation.onSucces(editText.getText().toString());
                }
            });
        builder.setNegativeButton("取消", new DialogCanelClickListener());
        builder.show();
    }

//    //两个事件 : 确定监听
//    class DialogSureClickListener implements DialogInterface.OnClickListener{
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            // 判断 点击的是什么按钮
//            dialog.dismiss();
//            String string = String.valueOf(editText.getText());
//
//            Toast.makeText(context,"点击了确定按钮"+ string,Toast.LENGTH_SHORT).show();
//            callBackInformation.onSucces("");
//        }
//    }

    //取消监听
    class DialogCanelClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // 判断 点击的是什么按钮
            dialog.dismiss();
            Toast.makeText(context,"点击了取消按钮",Toast.LENGTH_SHORT).show();
        }
    }

}
