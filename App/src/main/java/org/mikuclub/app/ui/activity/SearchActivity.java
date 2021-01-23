package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mikuclub.app.ui.activity.base.MyActivity;
import org.mikuclub.app.utils.http.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 * 搜索页面
 * search page
 */
public class SearchActivity extends MyActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 3;

        /* 组件 views */
        private EditText searchInput;
        private ImageView searchInputIcon;
        private FloatingActionButton floatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_search);
                //绑定组件
                Toolbar toolbar = findViewById(R.id.search_toolbar);
                searchInput = findViewById(R.id.search_input);
                searchInputIcon = findViewById(R.id.search_input_icon);
                floatingActionButton = findViewById(R.id.list_floating_action_button);

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }

        }


        /**
         * 监听标题栏菜单动作
         * listen toolbar item click event
         *
         * @param item
         * @return
         */
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {
                switch (item.getItemId())
                {
                        //如果点了返回键
                        case android.R.id.home:
                                //结束当前活动页
                                finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
        }


        public FloatingActionButton getFloatingActionButton()
        {
                return floatingActionButton;
        }

        public EditText getSearchInput()
        {
                return searchInput;
        }

        public ImageView getSearchInputIcon()
        {
                return searchInputIcon;
        }
}
