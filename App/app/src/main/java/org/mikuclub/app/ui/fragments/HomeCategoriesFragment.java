package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.CategoriesAdapter;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.PreferencesUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.UserUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页活动-分类碎片
 */
public class HomeCategoriesFragment extends Fragment
{


        //列表适配器
        private CategoriesAdapter recyclerViewAdapter;
        //列表数据
        private List<Category> recyclerDataList;


        /*组件*/
        //列表
        private RecyclerView recyclerView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_categories, container, false);

        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                recyclerView = view.findViewById(R.id.recycler_view);

                //从参数里获取分类信息
                String categoriesCache = PreferencesUtils.getCategoryPreference().getString(GlobalConfig.Preferences.CATEGORIES_CACHE, null);

                //反序列化
                recyclerDataList = ParserUtils.categories(categoriesCache).getBody();
                //检查用户是否登陆 , 没登陆的情况去除魔法区
                checkAndRemoveMofaCategory();

                //初始化数据列表
                initRecyclerView();

        }


        /**
         * 初始化文章列表
         */
        private void initRecyclerView()
        {
                //创建适配器
                recyclerViewAdapter = new CategoriesAdapter(recyclerDataList, getActivity());

                //创建列表网格布局
                //设置行数
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);

                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, recyclerDataList.size(), true, true, null);

        }

        /**
         * 检查用户是否登陆
         * 没登陆的情况去除魔法区
         */
        private void checkAndRemoveMofaCategory()
        {
                //如果用户未登陆, 从数组中去除 魔法区
                if (!UserUtils.isLogin())
                {
                        //从尾部开始遍历列表
                        for (int i = recyclerDataList.size() - 1; i >= 0; i--)
                        {
                                //如果抽到 魔法区分类
                                if (recyclerDataList.get(i).getId() == GlobalConfig.CATEGORY_ID_MOFA)
                                {
                                        //从列表中移除
                                        recyclerDataList.remove(i);
                                        //结束遍历
                                        i = 0;
                                }
                        }
                }
        }


}
