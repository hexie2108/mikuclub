package org.mikuclub.app.adapters.viewPager;

import org.mikuclub.app.ui.fragments.PostCommentsFragment;
import org.mikuclub.app.ui.fragments.PostMainFragment;
import org.mikuclub.app.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * viewpager 碎片适配器
 */
public class PostViewPagerAdapter extends FragmentStateAdapter
{
        private static final int FRAGMENT_NUMBER = 2;
        private PostMainFragment postMainFragment;

        public PostViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
        {
                super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {

                Fragment fragment = null;
                //根据位置 生成对应的 碎片
                switch (position)
                {
                        case 0:
                                //保存文章内容的碎片地址
                                postMainFragment = new PostMainFragment();
                                fragment=postMainFragment;
                                break;
                        case 1:
                                fragment = new PostCommentsFragment();
                                break;

                }


                return fragment;


        }

        @Override
        public int getItemCount()
        {
                return FRAGMENT_NUMBER;
        }

        public PostMainFragment getPostMainFragment()
        {
                return postMainFragment;
        }
}
