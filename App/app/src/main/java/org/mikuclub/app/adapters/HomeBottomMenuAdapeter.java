package org.mikuclub.app.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeBottomMenuAdapeter extends FragmentPagerAdapter
{

        private FragmentManager fragmentManager;
        private List<Fragment> fragments = new ArrayList<>();

        public HomeBottomMenuAdapeter(FragmentManager fm)
        {
                super(fm);
                this.fragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position)
        {
                return fragments.get(position);
        }

        @Override
        public int getCount()
        {
                return fragments.size();
        }

        public void addFragment(Fragment fragment)
        {
                fragments.add(fragment);
        }

        //due metodi da override per salvare lo stato di fragment durante il cambio della viewpage
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
                // 将实例化的fragment进行显示即可。
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                this.fragmentManager.beginTransaction().show(fragment).commit();
                return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
                Fragment fragment = fragments.get(position);
                fragmentManager.beginTransaction().hide(fragment).commit();
        }


}
