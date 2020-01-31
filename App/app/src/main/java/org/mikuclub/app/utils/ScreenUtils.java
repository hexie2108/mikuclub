package org.mikuclub.app.utils;

import android.app.Dialog;
import android.content.Context;
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
         * 给弹窗设置固定比例的高度
         *
         * @param context
         * @param view
         */

        public static void setFixWindowsHeight(Context context, final View view)
        {

                view.post(new Runnable()
                {
                        @Override
                        public void run()
                        {
                                GeneralUtils.setMaxHeightOfLayout(context, view, GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS);
                        }
                });
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
