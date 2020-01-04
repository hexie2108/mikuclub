package org.mikuclub.app.adapters.viewPager;

import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.ui.fragments.CategoryFragment;
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
public class CategoryViewPagerAdapter extends FragmentStateAdapter
{
        private Category category;

        public CategoryViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Category category)
        {
                super(fragmentActivity);
                this.category = category;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {

                Category currentCategory;
                //如果是第一个分页 就使用分类自己
                if (position == 0)
                {
                        currentCategory = category;
                }
                //如果是后续分页, 则使用分类的子分类
                else
                {
                        //因为0位置的分类是主分类自己, 所以要position-1 来获取子分类
                        currentCategory = category.getChildren().get(position - 1);
                }
                return CategoryFragment.startAction(currentCategory);



        }

        /**
         * 返回分页数
         *
         * @return
         */
        @Override
        public int getItemCount()
        {
                int count = 1;
                //如果不是null
                if (category.getChildren() != null)
                {
                        //加上 子分类的数量
                        count = count + category.getChildren().size();
                }
                return count;
        }


}
