package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.mikuclub.app.adapters.viewPager.CategoryViewPagerAdapter;
import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.utils.http.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 文章页
 */
public class CategoryActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 6;
        public static final String INTENT_CATEGORY = "category";

        /*变量*/
        private Category category;

        /*组件*/
        private Toolbar toolbar;
        private ImageView searchInputIcon;
        //分页菜单栏
        private TabLayout tabsMenuLayout;
        //分页管理器
        private ViewPager2 viewPager;
        private FloatingActionButton floatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_category);

                toolbar = findViewById(R.id.toolbar);
                searchInputIcon = findViewById(R.id.search_input_icon);
                viewPager = findViewById(R.id.category_view_pager);
                tabsMenuLayout = findViewById(R.id.category_tabs_menu);
                floatingActionButton = findViewById(R.id.list_floating_action_button);

                //获取文章数据
                category = (Category) getIntent().getSerializableExtra(INTENT_CATEGORY);

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //设置标题
                        actionBar.setTitle(category.getTitle());
                }


                initViewPager();

                initSearchIcon();

        }


        /**
         * 初始化 分页显示器
         */
        private void initViewPager()
        {
                viewPager.setAdapter(new CategoryViewPagerAdapter(this, category));
                new TabLayoutMediator(tabsMenuLayout, viewPager,
                        (tab, position) -> {
                                //第一个分页位
                                if (position == 0)
                                {
                                        tab.setText("全部");
                                }
                                //后续分页位
                                else
                                {
                                        //使用子分类名
                                        tab.setText(category.getChildren().get(position - 1).getTitle());
                                }
                        }).attach();
        }

        /**
         * 搜索图标绑定动作监听
         */
        private void initSearchIcon()
        {
                //绑定点击监听器到搜索栏
                searchInputIcon.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //启动搜索页面
                                SearchActivity.startAction(CategoryActivity.this);
                        }
                });
        }


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
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


        public FloatingActionButton getFloatingActionButton()
        {
                return floatingActionButton;
        }

        /**
         * 静态 启动本活动的方法
         *
         * @param context
         * @param category
         */
        public static void startAction(Context context, Category category)
        {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(INTENT_CATEGORY, category);
                context.startActivity(intent);
        }


}