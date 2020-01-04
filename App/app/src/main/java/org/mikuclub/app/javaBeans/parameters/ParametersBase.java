package org.mikuclub.app.javaBeans.parameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 空值请求
 * 只包含 _envelope 参数
 */
public class ParametersBase
{

        public Map<String, String> toMap()
        {

                Map<String, String> outputMap = new HashMap<String, String>();
                //追加参数, 让wordpress 在 回复body中增加页数信息, 不然会被加到 回复header头部里
                putIfNotNull(outputMap, "_envelope", "1");
                return outputMap;

        }
}
