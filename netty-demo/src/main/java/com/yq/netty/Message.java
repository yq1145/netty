package com.yq.netty;

import com.yq.pojo.TbChatRecord;

public class Message {
    private Integer type;//消息类型
    private TbChatRecord chatRecord;//消息信息
    private Object ext;//消息扩展

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
