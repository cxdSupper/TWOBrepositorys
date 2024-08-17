package com.cxd.cmcc.vo;

/**
 * @author caixd
 * @date 2024/8/14
 * @desc
 */
public class RecMsgVo {
    private String type;
    private String content;
    private String source;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "RecMsgVo{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
