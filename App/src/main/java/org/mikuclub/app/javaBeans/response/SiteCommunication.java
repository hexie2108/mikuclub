package org.mikuclub.app.javaBeans.response;


import java.io.Serializable;

public class SiteCommunication implements Serializable
{

        private SiteCommunicationBody body;
        private Integer status;


        public class SiteCommunicationBody
        {
                private String communication;
                private String app_adindex_01_text;
                private String app_adindex_01_link;
                private String app_adindex_01_show;

                public String getApp_adindex_01_show()
                {
                        return app_adindex_01_show;
                }

                public void setApp_adindex_01_show(String app_adindex_01_show)
                {
                        this.app_adindex_01_show = app_adindex_01_show;
                }

                public String getCommunication()
                {
                        return communication;
                }

                public void setCommunication(String communication)
                {
                        this.communication = communication;
                }

                public String getApp_adindex_01_text()
                {
                        return app_adindex_01_text;
                }

                public void setApp_adindex_01_text(String app_adindex_01_text)
                {
                        this.app_adindex_01_text = app_adindex_01_text;
                }

                public String getApp_adindex_01_link()
                {
                        return app_adindex_01_link;
                }

                public void setApp_adindex_01_link(String app_adindex_01_link)
                {
                        this.app_adindex_01_link = app_adindex_01_link;
                }
        }


        public SiteCommunicationBody getBody()
        {
                return body;
        }

        public void setBody(SiteCommunicationBody body)
        {
                this.body = body;
        }

        public Integer getStatus()
        {
                return status;
        }

        public void setStatus(Integer status)
        {
                this.status = status;
        }


}
