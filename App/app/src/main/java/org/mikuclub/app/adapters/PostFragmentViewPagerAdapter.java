package org.mikuclub.app.adapters;

import org.mikuclub.app.ui.fragments.CardFragment;
import org.mikuclub.app.ui.fragments.PostMainFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

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


                if (position == 0)
                {
                        return new PostMainFragment();
                }
                else
                {
                        return CardFragment.newInstance(position);
                }
        }

        @Override
        public int getItemCount()
        {
                return FRAGMENT_NUMBER;
        }


}
