package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.adapter.PrivateMessageAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.PrivateMessageController;
import org.mikuclub.app.delegate.MessageDelegate;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyTextWatcher;
import org.mikuclub.app.utils.http.Request;
import org.mikuclub.app.utils.storage.UserUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 私信页
 * private message page
 * <p>
 * 因为是 通过 fragment里的 viepager内置的fragment启动的活动
 * 导致了 onStop 和 onDestroy 异常, 会延迟10s左右才被加载,  不要在这2个函数内加任何代码
 */
public class PrivateMessageActivity extends AppCompatActivity
{

        /* 静态变量 Static variable */
        public static final int TAG = 11;
        public static final String INTENT_AUTHOR = "author";

        /* 变量 local variable */
        //数据请求代理人
        private MessageDelegate delegate;
        //数据控制器
        private PrivateMessageController controller;
        //列表适配器
        private PrivateMessageAdapter recyclerViewAdapter;
        //列表数据
        private List<PrivateMessage> recyclerDataList;
        private Author author;

        /* 组件 views */
        private Toolbar toolbar;
        private RecyclerView recyclerView;
        private TextInputLayout inputLayout;
        private TextInputEditText input;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_private_message);
                //绑定组件
                toolbar = findViewById(R.id.toolbar);
                recyclerView = findViewById(R.id.recycler_view);
                inputLayout = findViewById(R.id.input_layout);
                input = findViewById(R.id.input);

                //获取作者数据
                author = (Author) getIntent().getSerializableExtra(INTENT_AUTHOR);
                //创建数据请求 代理人
                delegate = new MessageDelegate(TAG);
                //初始化变量
                recyclerDataList = new ArrayList<>();

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //设置标题
                        actionBar.setTitle(author.getName());
                }

                //初始化列表
                initRecyclerView();
                //初始化控制器
                initController();

                //初始化消息输入框
                initInputForm();
        }

        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new PrivateMessageAdapter(recyclerDataList, this, UserUtils.getUser(), author);
                //创建列表主布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);

                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_MESSAGE * 2, false, true, null);
        }

        /**
         * 初始化控制器
         * init request controller
         */
        private void initController()
        {

                //创建数据控制器
                controller = new PrivateMessageController(this);
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);

                //设置私信作者id
                controller.setSenderId(author.getAuthor_id());
                //绑定输入框和输入框布局
                controller.setInput(input);
                controller.setInputLayout(inputLayout);

        }

        /**
         * 初始化文本输入框
         * init input form
         */
        private void initInputForm()
        {

                //只有不是系统消息的情况才能回复
                if (author.getAuthor_id() != 0)
                {
                        //创建点击事件监听器
                        View.OnClickListener onClickListener = v -> {
                                //发送私信
                                controller.sendMessage();
                        };

                        //input内容监听器, 在内容不为空的情况激活发送按钮并更改图标颜色
                        //自定义 text watcher, 只有在内容变化完成后才会激活回调
                        TextWatcher textWatcher = new MyTextWatcher(() -> {
                                String content = input.getText().toString().trim();
                                //如果内容不是空
                                if (!content.isEmpty())
                                {
                                        //激活按钮点击事件, 更换按钮颜色
                                        //绑定图标点击
                                        inputLayout.setEndIconOnClickListener(onClickListener);
                                        inputLayout.setEndIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                                }
                                else
                                {
                                        //注销按钮, 更换按钮颜色
                                        inputLayout.setEndIconOnClickListener(null);
                                        inputLayout.setEndIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.defaultTextColor)));
                                }
                        });
                        //添加内容变化监听器
                        input.addTextChangedListener(textWatcher);

                }
                else
                {
                        //禁止写私信给系统消息
                        inputLayout.setEnabled(false);
                }
        }


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
        protected void onStart()
        {
                super.onStart();
                //只有在没有数据的情况,
                if (recyclerDataList.isEmpty())
                {
                        //请求数据
                        controller.getMore();
                }
        }

        @Override
        protected void onPause()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onPause();
        }


        @Override
        protected void onStop()
        {
                /**
                 因为是 通过 fragment里的 viepager内置的fragment启动的活动
                 导致了 onStop 和 onDestroy 异常, 会延迟10s左右才被加载,  不要在这里加任何代码*/
                super.onStop();
        }

        @Override
        protected void onDestroy()
        {
                /**
                 因为是 通过 fragment里的 viepager内置的fragment启动的活动
                 导致了 onStop 和 onDestroy 异常, 会延迟10s左右才被加载,  不要在这里加任何代码*/
                super.onDestroy();
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param author
         */
        public static void startAction(Context context, Author author)
        {
                Intent intent = new Intent(context, PrivateMessageActivity.class);
                intent.putExtra(INTENT_AUTHOR, author);
                context.startActivity(intent);
        }


}
