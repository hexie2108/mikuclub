package org.mikuclub.app.javaBeans.response;


import java.io.Serializable;

/**
 * 基础通用respond解析类
 */
public class SingleResponse  implements Serializable
{
        private String body;
        private Integer status;

        public String getBody()
        {
                return body;
        }

        public void setBody(String body)
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
