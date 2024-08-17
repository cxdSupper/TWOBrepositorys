package com.cxd.cmcc.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caixd
 * @date 2024/8/14
 * @desc
 */
public class MsgResponse implements Serializable {
    private boolean success;
    private Map<String, Object> data;



    public static MsgResponse resp(boolean reply,String content) {
        MsgResponse msgResponse = new MsgResponse();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "text");
        map.put("content", content==null ? "" : content);
        msgResponse.setSuccess(reply);
        msgResponse.setData(map);

        return msgResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
