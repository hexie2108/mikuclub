package org.mikuclub.app.utils.custom;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 自定义gridlayout 每个item使用列数的策略
 * Custom gridlayout strategy for using number of columns per item
 */
public class MyGridLayoutSpanSizeLookup extends  GridLayoutManager.SpanSizeLookup
{

        //是否有头部
        //Is there a head
        private boolean isHeader = false;

        private List dataList;
        private int columnsNumberToSpan;


        /**
         *构造器
         * 默认 就是开启尾部组件占位支持,  可选是否开启头部占位支持
         *in default tail, the item is present, optional whether to enable the head item
         * @param dataList
         * @param columnsNumberToSpan
         * @param isHeader 如果 true 则会为头部安排位置, false 则不为头部预留位置
         */
        public MyGridLayoutSpanSizeLookup(List dataList, int columnsNumberToSpan, boolean isHeader)
        {
                this.dataList = dataList;
                this.columnsNumberToSpan = columnsNumberToSpan;
                this.isHeader = isHeader;
        }


        /**
         * 获取每个item所占的行数
         * Get the number of rows occupied by each item
         * @param position
         * @return
         */
        @Override
        public int getSpanSize(int position)
        {
                //默认每个视图占1列
                int columns = 1;
                //头部占用的行数, 有开启头部的话 就是占用1行
                int headerRow = isHeader ? 1 : 0;
                //只有在位置 是头部 或者 尾部的时候 才需要获取特别的占列数量
                if(position == headerRow-1 ||  position == (dataList.size()+headerRow)){
                        columns = columnsNumberToSpan;
                }

                return columns;
        }
}
