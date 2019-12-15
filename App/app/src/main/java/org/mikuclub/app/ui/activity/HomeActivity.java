package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import org.mikuclub.app.adapters.HomeBottomMenuAdapeter;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.fragments.BaseFragment;
import org.mikuclub.app.ui.fragments.HomeMainFragment;
import org.mikuclub.app.utils.http.Request;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class HomeActivity extends AppCompatActivity
{
        public static final int TAG = 2;

        private PostDelegate homePresenter;
        private TextView textView;

        private BottomNavigationView bottomNavigationView;
        private ViewPager viewPager;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.home_activity);


                homePresenter = new PostDelegate(TAG);



                initViewPager();
                initBottomMenuView();


        }

        /**
         * initial bottom menu
         */
        private void initBottomMenuView()
        {
                //get menu view
                bottomNavigationView = (BottomNavigationView) findViewById(R.id.homeBottomMenuView);
                //listener on bottom menu item
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item)
                        {
                                switch (item.getItemId())
                                {
                                        case R.id.item_home:
                                                viewPager.setCurrentItem(0);
                                                return true;

                                        case R.id.item_category:
                                                viewPager.setCurrentItem(1);
                                                return true;

                                        case R.id.item_create:
                                                viewPager.setCurrentItem(2);
                                                return true;

                                        case R.id.item_user:
                                                viewPager.setCurrentItem(3);
                                                return true;
                                }
                                return false;
                        }
                });


        }

        /**
         * initial view pager
         */
        private void initViewPager()
        {
                //get contenitore di viewPage
                viewPager = (ViewPager) findViewById(R.id.homeMainViewPager);
                //setup viewPager
                HomeBottomMenuAdapeter adapter = new HomeBottomMenuAdapeter(getSupportFragmentManager());
                adapter.addFragment(new HomeMainFragment());
                adapter.addFragment(BaseFragment.getInstance(R.layout.home_fragment_bottom_menu_item));
                adapter.addFragment(BaseFragment.getInstance(R.layout.home_fragment_bottom_menu_item));
                adapter.addFragment(BaseFragment.getInstance(R.layout.home_fragment_bottom_menu_item));
                viewPager.setAdapter(adapter);

                //listener on ViewPager
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
                {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                        {

                        }

                        @Override
                        public void onPageSelected(int position)
                        {
                                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state)
                        {

                        }
                });

                //listener per disattivare cambio di ViewPager  tramite scorrimento
                viewPager.setOnTouchListener(new View.OnTouchListener()
                {
                        @Override
                        public boolean onTouch(View v, MotionEvent event)
                        {
                                return true;
                        }
                });
        }


        @Override
        protected void onStop()
        {
                super.onStop();

                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);

        }




        /*
        public void onClickHandlerOnImage(View view){
                ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(HomeActivity.this), new ImageFileCache());
                NetworkImageView networkImageView = findViewById(R.id.img1);
                networkImageView.setDefaultImageResId(R.drawable.a);
                networkImageView.setErrorImageResId(R.drawable.c);
                networkImageView.setImageUrl( "https://static.mikuclub.org/wp-content/uploads/2019/06/20190603081513-500x280.jpg", imageLoader);
        }*/


        /**
         * 静态 启动本活动的方法
         * @param context
         * @param stickyPostList
         * @param postList
         */
        public static void startAction(Context context, Posts stickyPostList, Posts postList){

                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("sticky_post_list", stickyPostList);
                intent.putExtra("post_list", postList);
                context.startActivity(intent);
        }


}
