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
        private String access_token;
        private String open_type;
        private String union_id;
        private String open_id;

        @Override
        public Map<String, Object> toMap()
        {

                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "username", username);
                putIfNotNull(outputMap, "password", password);
                putIfNotNull(outputMap, "access_token", access_token);
                putIfNotNull(outputMap, "open_type", open_type);
                putIfNotNull(outputMap, "union_id", union_id);
                putIfNotNull(outputMap, "open_id", open_id);


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

        public String getAccess_token()
        {
                return access_token;
        }

        public void setAccess_token(String access_token)
        {
                this.access_token = access_token;
        }

        public String getOpen_type()
        {
                return open_type;
        }

        public void setOpen_type(String open_type)
        {
                this.open_type = open_type;
        }


        public String getUnion_id()
        {
                return union_id;
        }

        public void setUnion_id(String union_id)
        {
                this.union_id = union_id;
        }

        public String getOpen_id()
        {
                return open_id;
        }

        public void setOpen_id(String open_id)
        {
                this.open_id = open_id;
        }
}
