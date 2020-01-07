package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class LoginParameters extends BaseParameters
{
        private String username;
        private String password;

        @Override
        public Map<String, String> toMap()
        {

                Map<String, String> outputMap = new HashMap<String, String>();

                putIfNotNull(outputMap, "username", username);
                putIfNotNull(outputMap, "password", password);


                return outputMap;
        }

        public String getUsername()
        {
                return username;
        }

        public void setUsername(String username)
        {
                this.username = username;
        }

        public String getPassword()
        {
                return password;
        }

        public void setPassword(String password)
        {
                this.password = password;
        }
}
