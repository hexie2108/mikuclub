package org.mikuclub.app.javaBeans.response.modules;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Headers implements Serializable
{
        @SerializedName("X-WP-Total")
        private int totalNumber;
        @SerializedName("X-WP-TotalPages")
        private int totalPage;

        public int getTotalNumber()
        {
                return totalNumber;
        }

        public void setTotalNumber(int totalNumber)
        {
                this.totalNumber = totalNumber;
        }

        public int getTotalPage()
        {
                return totalPage;
        }

        public void setTotalPage(int totalPage)
        {
                this.totalPage = totalPage;
        }
}
