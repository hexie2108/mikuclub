package org.mikuclub.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.mikuclub.app.config.GlobalConfig;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;

/**
 * 屏幕相关实用类
 * Screen related utility class
 */
public class ScreenUtils
{

        /**
         * 判断当前设备是否处于横屏状态
         * @param context
         * @return
         */
        public static boolean isHorizontal(Context context){

                boolean screenIsHorizontal = false;
                int orientation = context.getResources().getConfiguration().orientation;
                //如果获取到的识别码是 代表横屏
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                        screenIsHorizontal = true;
                }
                return screenIsHorizontal;

        }


        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        public static int getScreenWidth(Context context)
        {
                WindowManager wm = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics outMetrics = new DisplayMetrics();
                if (wm != null)
                {
                        wm.getDefaultDisplay().getMetrics(outMetrics);
                }

                return outMetrics.widthPixels;
        }

        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        public static int getScreenHeight(Context context)
        {
                WindowManager wm = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);

                DisplayMetrics outMetrics = new DisplayMetrics();

                if (wm != null)
                {
                        wm.getDefaultDisplay().getMetrics(outMetrics);
                }

                return outMetrics.heightPixels;
        }


        /**
         * 设置布局的高度占比
         *
         * @param context
         * @param view
         */
        public static void setHeightForWindowsFragment(Context context, View view)
        {

                //获取屏幕宽高 (px)
                int height = context.getResources().getDisplayMetrics().heightPixels;
                //判断是否是处于横屏状态
                if (!isHorizontal(context))
                {
                        //设置窗口的高度 (相对于屏幕的百分比)
                        height = (int) (height * GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS_VERTICAL); //设置高度为屏幕百分比
                }
                else
                {
                        //设置窗口的高度 (相对于屏幕的百分比)
                        height = (int) (height * GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS_HORIZONTAL); //设置高度为屏幕百分比
                }
                setViewSize(view, 0, height);

        }

        /**
         * 设置 view组件的大小
         *
         * @param view
         * @param width  设置为0 则将继续使用原来的宽度
         * @param height 设置为0 则将继续使用原来的高度
         */
        public static void setViewSize(View view, int width, int height)
        {
                //在组件生成后 再执行, 避免空指针错误
                view.post(() -> {
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        if (width > 0)
                        {
                                layoutParams.width = width;
                        }
                        if (height > 0)
                        {
                                layoutParams.height = height;
                        }
                        view.setLayoutParams(layoutParams);
                });
        }


        /**
         * 禁用 SheetDialog弹窗的拖拽功能
         *
         * @param dialog
         */
        public static void disableDraggingOfBottomSheetDialogFragment(Dialog dialog)
        {

                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                LinearLayout layout = new LinearLayout(dialog.getContext());
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                bottomSheetDialog.setContentView(layout);

                try
                {
                        Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
                        behaviorField.setAccessible(true);
                        final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
                        if (behavior != null)
                        {
                                //设置默认就展开最大高度
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                //绑定动作, 禁止拖拽改变大小
                                behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
                                {
                                        @Override
                                        public void onStateChanged(@NonNull View bottomSheet, int newState)
                                        {
                                                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                                                {
                                                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                                }
                                        }

                                        @Override
                                        public void onSlide(@NonNull View bottomSheet, float slideOffset)
                                        {
                                        }
                                });
                        }
                }
                catch (NoSuchFieldException e)
                {
                        e.printStackTrace();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }


}
