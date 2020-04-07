package org.mikuclub.app.javaBeans.response;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * 基础通用respond解析类
 */
public class SingleResponseArrayInteger implements Serializable
{
        private ArrayList<Integer> body;
        private Integer status;

        public ArrayList<Integer> getBody()
        {
                return body;
        }

        public void setBody(ArrayList<Integer> body)
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
