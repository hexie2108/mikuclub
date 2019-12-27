package org.mikuclub.app.adapters;

import org.mikuclub.app.ui.fragments.PostCommentsFragment;
import org.mikuclub.app.ui.fragments.PostMainFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * viewpager 碎片适配器
 */
public class PostFragmentViewPagerAdapter extends FragmentStateAdapter
{
        private static final int FRAGMENT_NUMBER = 2;

        public PostFragmentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
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
                                fragment = new PostMainFragment();
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


}
