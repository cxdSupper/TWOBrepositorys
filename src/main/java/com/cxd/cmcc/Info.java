package com.cxd.cmcc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caixd
 * @date 2023/9/22
 * @desc
 */
public enum Info {
//    yjw("18307073787","Y/BGNmwuTXmQ2tRGXNuiTy7izNx0tu3eIPqzz4+EfNd/hewC/4AtGQ\u003d\u003d","易金伟"),
    ll("15279772652","ca8c80a7b293cb1bbc932ebe4b66604d","ll"),
    cxd("15170252724","0655f9034673a6264902e7981180ef0d","cxd")
    ;

    public static Map<String, Info> infoMap = new HashMap<>();
    static {
        Info[] types = Info.values();
        for (Info type : types) {
            infoMap.put(type.name() , type);
        }
    }
    private String phone;
    private String token;
    private String name;

    Info(String phone, String token,String name) {
        this.token = token;
        this.phone = phone;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}
