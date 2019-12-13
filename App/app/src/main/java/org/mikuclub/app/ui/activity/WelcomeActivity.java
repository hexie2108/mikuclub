package org.mikuclub.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

import android.os.Bundle;


/**
 * 启动页面
 * launch activity
 */
public class WelcomeActivity extends AppCompatActivity
{

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);
        }


}
