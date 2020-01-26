package org.mikuclub.app.javaBeans.response;


import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;

public class WpError implements Serializable
{

        private ErrorBody body;
        private Integer status;
        private Headers headers;

        public class ErrorBody
        {
                private String code;
                private String message;

                public String getCode()
                {
                        return code;
                }

                public void setCode(String code)
                {
                        this.code = code;
                }

                public String getMessage()
                {
                        return message;
                }

                public void setMessage(String message)
                {
                        this.message = message;
                }
        }


        public ErrorBody getBody()
        {
                return body;
        }

        public void setBody(ErrorBody body)
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

        public Headers getHeaders()
        {
                return headers;
        }

        public void setHeaders(Headers headers)
        {
                this.headers = headers;
        }
}