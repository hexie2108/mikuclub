package org.mikuclub.app.javaBeans.resources;


import java.io.Serializable;

public class WpError implements Serializable
{

        private ErrorBody body;
        private int status;

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

        public int getStatus()
        {
                return status;
        }

        public void setStatus(int status)
        {
                this.status = status;
        }
}
