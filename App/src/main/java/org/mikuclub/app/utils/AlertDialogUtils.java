package org.mikuclub.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

/**
 * 自定义弹窗管理器
 * 用来快速创建特定的弹窗 (加载进度条, 重试窗口等等)
 * Custom AlertDialog manager
 * Used to quickly create specific popup
 */
public class AlertDialogUtils
{

        /**
         * 创建进度条弹窗
         */
        public static AlertDialog createProgressDialog(Context context, boolean cancelable, boolean forceFinish)
        {
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(context);
                builder.setView(R.layout.alert_dialog_progress_bar);
                builder.setCancelable(cancelable);
                AlertDialog dialog = builder.create();
                //禁止通过触摸屏幕关闭弹窗
                //禁止通过触摸屏幕关闭弹窗
                dialog.setCanceledOnTouchOutside(cancelable);
                //只有在可取消的情况下
                if (cancelable)
                {
                        //需要重写弹窗的按钮监听
                        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
                                if (keyCode == KeyEvent.KEYCODE_BACK)
                                {
                                        dialog.dismiss();
                                        //如果是 活动上下文的话
                                        if (forceFinish && context instanceof AppCompatActivity)
                                        {
                                                //结束相关活动
                                                ((AppCompatActivity) context).finish();
                                        }
                                }
                                return true;
                        });
                }
                return dialog;
        }


        /**
         * 创建通用询问弹窗
         *
         * @param context
         * @param title
         * @param content
         * @param positiveButtonText
         * @param positiveClickListener
         * @return
         */
        public static AlertDialog createConfirmDialog(Context context, String title, String
                content, boolean cancelable, boolean forceFinish, String
                                                              positiveButtonText, DialogInterface.OnClickListener positiveClickListener, String negativeButtonText, DialogInterface.OnClickListener negativeClickListener)
        {
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(context);
                builder.setCancelable(cancelable);
                builder.setTitle(title);
                if (content != null)
                {
                        builder.setMessage(content);
                }
                //设置确认按钮
                builder.setPositiveButton(positiveButtonText, positiveClickListener);
                //设置取消按钮
                builder.setNegativeButton(negativeButtonText, negativeClickListener);
                //创建弹窗
                AlertDialog dialog = builder.create();
                //禁止通过触摸屏幕关闭弹窗
                dialog.setCanceledOnTouchOutside(cancelable);
                //只有在可取消的情况下
                if (cancelable)
                {
                        //需要重写弹窗的按钮监听
                        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
                                if (keyCode == KeyEvent.KEYCODE_BACK)
                                {
                                        dialog.dismiss();
                                        //如果 可以强制取消 而且 是活动上下文的话
                                        if (forceFinish && context instanceof AppCompatActivity)
                                        {
                                                //结束相关活动
                                                ((AppCompatActivity) context).finish();
                                        }
                                }
                                return true;
                        });
                }

                return dialog;
        }

        /**
         * 创建删除确认弹窗
         *
         * @return
         */
        public static AlertDialog createDeleteConfirmDialog(Context context, DialogInterface.OnClickListener positiveClickListener)
        {
                return createConfirmDialog(context, ResourcesUtils.getString(R.string.confirm_delete), null, true, false, ResourcesUtils.getString(R.string.confirm), positiveClickListener, ResourcesUtils.getString(R.string.cancel), null);

        }


}
