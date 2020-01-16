package org.mikuclub.app.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import org.mikuclub.app.contexts.MyApplication;

public class ClipboardUtils
{
        //获取剪贴板管理器
        private static ClipboardManager clipboardManager;

        /**
         * 设置字符串到剪切板里
         */
        public static void setText(String text){

                //如果还不存在
                if(clipboardManager ==null){
                        clipboardManager = (ClipboardManager) MyApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                }
                //创建字符剪切内容
                ClipData clipData = ClipData.newPlainText("", text);
                //添加到剪切板
                clipboardManager.setPrimaryClip(clipData);

        }


}
