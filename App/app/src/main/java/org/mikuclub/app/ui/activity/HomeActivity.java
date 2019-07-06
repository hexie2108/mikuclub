package org.mikuclub.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import org.mikuclub.app.presenter.HomePresenter;
import org.mikuclub.app.adapters.HomeBottomMenuAdapeter;
import org.mikuclub.app.ui.fragments.BaseFragment;
import org.mikuclub.app.ui.fragments.HomeMainFragment;
import org.mikuclub.app.utils.PermissionCheck;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class HomeActivity extends AppCompatActivity
{

        private HomePresenter homePresenter;
        private TextView textView;

        private BottomNavigationView bottomNavigationView;
        private ViewPager viewPager;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.home_activity);


                homePresenter = new HomePresenter();

                //check storage permission
                PermissionCheck.verifyStoragePermissions(this);

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
                //cancella tutte le richieste incorso
                //homePresenter.cancelRequest();
        }


        /**
         * callback dopo utente ha risposto la richiesta di permessi
         *
         * @param requestCode
         * @param permissions
         * @param grantResults
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
        {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        }


        /*
        public void onClickHandlerOnImage(View view){
                ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(HomeActivity.this), new ImageFileCache());
                NetworkImageView networkImageView = findViewById(R.id.img1);
                networkImageView.setDefaultImageResId(R.drawable.a);
                networkImageView.setErrorImageResId(R.drawable.c);
                networkImageView.setImageUrl( "https://static.mikuclub.org/wp-content/uploads/2019/06/20190603081513-500x280.jpg", imageLoader);
        }*/


        /*
   function to start the searchActivity
   * */
        public void startSearchActivity(){
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);


        }



}
