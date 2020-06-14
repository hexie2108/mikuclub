package org.mikuclub.app.ui.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 自定义fragment
 * 增加了命周期的日志输出功能, 方便debug
 * custom fragment class
 * Added the log function on the life cycle to facilitate debugging
 */
public class MyFragment extends Fragment
{
        @Override
        public void onAttach(@NonNull Context context)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onAttach");
                super.onAttach(context);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onCreate");
                super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onCreateView");
                return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onViewCreated");
                super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onActivityCreated");
                super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onStart()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStart");
                super.onStart();
        }

        @Override
        public void onResume()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onResume");
                super.onResume();
        }


        @Override
        public void onPause()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onPause");
                super.onPause();
        }

        @Override
        public void onStop()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStop");
                super.onStop();
        }


        @Override
        public void onDestroyView()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onDestroyView");
                super.onDestroyView();
        }

        @Override
        public void onDestroy()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onDestroy");
                super.onDestroy();
        }

        @Override
        public void onDetach()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onDetach");
                super.onDetach();
        }
}
