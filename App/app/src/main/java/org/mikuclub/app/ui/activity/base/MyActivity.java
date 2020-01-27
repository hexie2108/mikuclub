package org.mikuclub.app.ui.activity.base;

import android.os.Bundle;

import org.mikuclub.app.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity
{

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onCreate");
                super.onCreate(savedInstanceState);
        }

        @Override
        protected void onStart()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStart");
                super.onStart();
        }

        @Override
        protected void onResume()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onResume");
                super.onResume();
        }


        @Override
        protected void onPause()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onPause");
                super.onPause();
        }

        @Override
        protected void onStop()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStop");
                super.onStop();
        }

        @Override
        protected void onRestart()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onRestart");
                super.onRestart();
        }

        @Override
        protected void onDestroy()
        {
                LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onDestroy");
                super.onDestroy();
        }
}
