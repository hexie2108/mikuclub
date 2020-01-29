package org.mikuclub.app.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘和焦点管理器
 * Keyboard and focus manager
 */
public class KeyboardUtils
{

        /**
         * 显示键盘+获取焦点
         * @param view
         */
        public static void showKeyboard(final View view)
        {
                final InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                        //因为系统原因, 需要延迟运行才能正常
                        view.postDelayed(() -> {
                                view.requestFocus();
                                imm.showSoftInput(view, 0);
                        }, 100);

                }
        }

        /**
         * 硬盘键盘+取消焦点
         * @param view
         */
        public static void hideKeyboard(View view)
        {
                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                        view.clearFocus();
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
        }

        /**
         * 切换键盘显示
         * @param view
         */
        public static void toggleSoftInput(View view)
        {
                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                        imm.toggleSoftInput(0, 0);
                }
        }
}
