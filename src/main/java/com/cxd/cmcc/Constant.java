package com.cxd.cmcc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caixd
 * @date 2025/8/18
 * @desc
 */
public class Constant {

    static Map<String, Object> constantMap = new HashMap<>();

    public static Object getValue(String key){
        return constantMap.get(key);
    }

    public static void setValue(String key, Object value){
        constantMap.put(key, value);
    }

}
