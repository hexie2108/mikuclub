package org.mikuclub.app.view;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 自定义gridlayout 每行使用的列数
 */
public class CustomGridLayoutSpanSizeLookup extends  GridLayoutManager.SpanSizeLookup
{

        //头部占据的行数, 默认不存在, 所以0行
        private boolean isHeader = false;

        private List dataList;
        private int columnsNumberToSpan;


        /**
         *
         * 默认带一个尾部
         * @param dataList
         * @param columnsNumberToSpan
         * @param isHeader 如果 true 则会为头部安排位置, false 则不为头部预留位置
         */
        public CustomGridLayoutSpanSizeLookup(List dataList, int columnsNumberToSpan, boolean isHeader)
        {
                this.dataList = dataList;
                this.columnsNumberToSpan = columnsNumberToSpan;
                this.isHeader = isHeader;
        }


        @Override
        public int getSpanSize(int position)
        {
                //默认每个视图占1列
                int columns = 1;
                //额外多出来的行数, 有头部的话 就是 1
                int extraRow = isHeader ? 1 : 0;
                //position从0开始, 所以只有碰到 有头部 或者 到达尾部的时候 才算满足条件
                if(position == extraRow-1 ||  position == (dataList.size()+extraRow)){
                        columns = columnsNumberToSpan;
                }

                return columns;
        }
}
