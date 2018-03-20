package com.mycompany.ui.Dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mycompany.ui.R;

/**
 * Created by qkz on 2018/3/16.
 */

public class OpenBleDialog {
    private AlertDialog dialog;
    private Activity activity;

    public OpenBleDialog(Activity activity){
        this.activity = activity;
        dialog = new AlertDialog.Builder(activity).create();
    }
    public void show(final AlerDialogCallBack alerDialogCallBack) {
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_open_ble);
        window.findViewById(R.id.layout_dialog_openble_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_openble_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialogCallBack.success();
                dialog.dismiss();
            }
        });
    }
}
