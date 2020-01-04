package org.mikuclub.app.javaBeans;

public class WordpressError
{

        private Body body;
        private int status;
        private Headers headers;


        public class Body
        {
                String code;
                String message;
                Data data;


        }

        public class Data{
                int status;
        }

        public class Headers{
                String Allow;
        }

}
