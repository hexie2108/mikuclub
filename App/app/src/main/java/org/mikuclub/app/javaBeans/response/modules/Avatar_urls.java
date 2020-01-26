package org.mikuclub.app.javaBeans.response.modules;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Avatar_urls implements Serializable
{

        @SerializedName("24")
        private String size24;
        @SerializedName("48")
        private String size48;
        @SerializedName("96")
        private String size96;

        public String getSize24()
        {
                return size24;
        }

        public void setSize24(String size24)
        {
                this.size24 = size24;
        }

        public String getSize48()
        {
                return size48;
        }

        public void setSize48(String size48)
        {
                this.size48 = size48;
        }

        public String getSize96()
        {
                return size96;
        }

        public void setSize96(String size96)
        {
                this.size96 = size96;
        }


}
