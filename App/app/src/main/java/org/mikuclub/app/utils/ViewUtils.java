package org.mikuclub.app.utils;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AlertDialog;
import mikuclub.app.R;

public class ViewUtils
{

        /**
         * 创建进度条弹窗
         */
        public static AlertDialog initProgressDialog(Context context){
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(context);
                builder.setView(R.layout.alert_dialog_progress_bar);
                builder.setCancelable(false);
                return builder.create();
        }


}
