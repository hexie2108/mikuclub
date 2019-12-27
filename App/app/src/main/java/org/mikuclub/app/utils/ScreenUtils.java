package org.mikuclub.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import mikuclub.app.R;

public class ScreenUtils
{

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
                wm.getDefaultDisplay().getMetrics(outMetrics);
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
                wm.getDefaultDisplay().getMetrics(outMetrics);
                return outMetrics.heightPixels;
        }


        public static void disableDraggingOfBottomSheetDialogFragment(Dialog dialog) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                bottomSheetDialog.setContentView(R.layout.empty);

                try {
                        Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
                        behaviorField.setAccessible(true);
                        final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
                        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                @Override
                                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                        if (newState == BottomSheetBehavior.STATE_DRAGGING){
                                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                        }
                                }
                                @Override
                                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                                }
                        });
                } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                }
        }


}
