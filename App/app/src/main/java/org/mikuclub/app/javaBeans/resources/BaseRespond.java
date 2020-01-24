package org.mikuclub.app.javaBeans.resources;


import java.io.Serializable;

/**
 * 基础通用respond解析类
 */
public class BaseRespond implements Serializable
{

        private String body;
        private int status;

        public String getBody()
        {
                return body;
        }

        public void setBody(String body)
        {
                this.body = body;
        }

        public int getStatus()
        {
                return status;
        }

        public void setStatus(int status)
        {
                this.status = status;
        }
}
