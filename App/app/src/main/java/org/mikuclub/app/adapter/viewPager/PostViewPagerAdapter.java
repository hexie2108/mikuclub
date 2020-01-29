package org.mikuclub.app.adapter.viewPager;

import org.mikuclub.app.ui.fragments.PostCommentsFragment;
import org.mikuclub.app.ui.fragments.PostMainFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 文章页面 viewpager 碎片适配器
 * post page, fragment adapter for viewpager
 */
public class PostViewPagerAdapter extends FragmentStateAdapter
{
        private static final int FRAGMENT_NUMBER = 2;
        //用来储存fragment的位置, 方便之后获取
        private PostMainFragment postMainFragment;

        public PostViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
        {
                super(fragmentActivity);
        }

        /**
         * 创建fragment
         * create fragment
         *
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
                Fragment fragment;
                //如果是第一个分页
                if (position == 0)
                {
                        //保存文章内容的碎片地址
                        postMainFragment = new PostMainFragment();
                        fragment = postMainFragment;
                }
                //如果是第二个分页
                else
                {
                        fragment = new PostCommentsFragment();
                }

                return fragment;
        }

        /**
         * 返回需要创建的分页数量
         * Returns the number of fragment to create
         *
         * @return
         */
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
