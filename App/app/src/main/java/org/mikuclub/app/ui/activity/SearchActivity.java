package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.http.Request;

import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 *  搜索页面
 */
public class SearchActivity extends AppCompatActivity
{

        public static final int TAG = 3;
        private EditText searchInput;
        private ImageView searchInputIcon;
        private FloatingActionButton listFloatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_search);

                //替换原版标题栏
                Toolbar toolbar = findViewById(R.id.search_toolbar);
                searchInput =findViewById(R.id.search_input);
                searchInputIcon = findViewById(R.id.search_input_icon);
                listFloatingActionButton = findViewById(R.id.list_floating_action_button);

                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if(actionBar != null){
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);

                }




        }



        //监听标题栏菜单动作
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {

                switch (item.getItemId()){
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

        public FloatingActionButton getListFloatingActionButton()
        {
                return listFloatingActionButton;
        }

        /**
         * 静态 启动本活动的方法
         *
         * @param context
         */
        public static void startAction(Context context)
        {

                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
        }

}
