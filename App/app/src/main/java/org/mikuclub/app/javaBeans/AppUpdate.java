package org.mikuclub.app.javaBeans;

public class AppUpdate
{

        private int versionCode;
        private String versionName;
        private String description;
        private boolean forceUpdate;
        private String downUrl;

        public String getDownUrl()
        {
                return downUrl;
        }

        public void setDownUrl(String downUrl)
        {
                this.downUrl = downUrl;
        }

        public int getVersionCode()
        {
                return versionCode;
        }

        public void setVersionCode(int versionCode)
        {
                this.versionCode = versionCode;
        }

        public String getVersionName()
        {
                return versionName;
        }

        public void setVersionName(String versionName)
        {
                this.versionName = versionName;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public boolean isForceUpdate()
        {
                return forceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate)
        {
                this.forceUpdate = forceUpdate;
        }
}
