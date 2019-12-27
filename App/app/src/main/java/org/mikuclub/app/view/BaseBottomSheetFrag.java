package org.mikuclub.app.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.mikuclub.app.utils.LogUtils;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * Created by 李可乐 on 2016/9/8 0008.
 * https://blog.csdn.net/card361401376/article/details/53537629
 */
public abstract class BaseBottomSheetFrag extends BottomSheetDialogFragment
{

        protected Context mContext;

        protected View rootView;
        protected BottomSheetBehavior mBehavior;

        protected Dialog dialog;

        @Override public void onAttach(Context context) {
                super.onAttach(context);
                this.mContext = context;
        }

        @Override public void onStart() {
                super.onStart();
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        @Override public void onDestroyView() {
                super.onDestroyView();
                //解除缓存View和当前ViewGroup的关联
                ((ViewGroup) (rootView.getParent())).removeView(rootView);
        }

        @NonNull
        @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
                //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
                dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
                if (rootView == null) {
                        //缓存下来的View 当为空时才需要初始化 并缓存
                        rootView = View.inflate(mContext, getLayoutResId(), null);
                        initView();
                }

                setContentView(dialog);//设置View重新关联
                mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
                mBehavior.setHideable(true);
                //让父View背景透明 是圆角边的关键
                ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);

                rootView.post(new Runnable() {
                        @Override public void run() {
                                /**
                                 * PeekHeight默认高度256dp 会在该高度上悬浮
                                 * 设置等于view的高 就不会卡住
                                 */
                                mBehavior.setPeekHeight(rootView.getHeight());
                        }
                });

                resetView();

                return dialog;
        }

        /**
         * 设置显示的View到Dialog中
         * 抽象方法 子类可重写
         * 默认添加的View 高度为Wrap 某些场景需要固定高度
         * @param dialog
         */
        protected void setContentView(Dialog dialog) {
                dialog.setContentView(rootView);
        }

        public abstract int getLayoutResId();

        /**
         * 初始化View和设置数据等操作的方法
         */
        public abstract void initView();

        /**
         * 重置的View和数据的空方法 子类可以选择实现
         * 为避免多次inflate 父类缓存rootView
         * 所以不会每次打开都调用{@link #initView()}方法
         * 但是每次都会调用该方法 给子类能够重置View和数据
         */
        public void resetView() {

        }

        public boolean isShowing() {
                return dialog != null && dialog.isShowing();
        }

        /**
         * 使用关闭弹框 是否使用动画可选
         * 使用动画 同时切换界面Aty会卡顿 建议直接关闭
         */
        public void close(boolean isAnimation) {
                if (isAnimation) {
                        if (mBehavior != null) {
                                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                } else {
                        dismiss();
                }
        }

        @Override public void show(FragmentManager manager, String tag) {
                if (!this.isAdded()) {
                        super.show(manager, tag);
                } else {
                        LogUtils.d(this + " has add to FragmentManager");
                }
        }
}
