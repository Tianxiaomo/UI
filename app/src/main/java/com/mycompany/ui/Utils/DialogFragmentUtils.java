package com.mycompany.ui.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.mycompany.ui.R;

/**
 * Created by qkz on 2018/1/12.
 */

public class DialogFragmentUtils extends DialogFragment
{

    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        Button button ;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        //.setPositiveButton(R.id.negtive,null);
                // Add action buttons
//                .setPositiveButton("Sign in",
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id)
//                            {
//                            }
//                        }).setNegativeButton("Cancel", null);
        //AlertDialog.BUTTON_POSITIVE = R.id.negtive;
//        AlertDialog dialog = builder.setTitle("aaa").setMessage("Please enter Date")
//                . setPositiveButton("OK", null).setNegativeButton("Cancel", null).create();
//        dialog.show();
        //dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, 600);
//        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        positiveButton.setY(positiveButton.getY()-100);
//        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        negativeButton.setY(negativeButton.getY()-100);
//        return builder.create();

        AlertDialog dialog = builder.create();
        dialog.setButton(R.id.negtive, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return dialog;
    }
}