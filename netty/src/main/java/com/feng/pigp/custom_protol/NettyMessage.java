package com.feng.pigp.custom_protol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feng
 * @date 2019/5/13 11:11
 * @since 1.0
 */
public final class NettyMessage {

    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public final static class Header{

        private int crcCode = 0xabef0101; //固定内容+主版本号+次版本号
        private int length; //消息总长度
        private long sessionID;
        private byte type; //类型
        private byte priority; //优先级
        private Map<String, Object> attachment = new HashMap<>();

        public int getCrcCode() {
            return crcCode;
        }

        public void setCrcCode(int crcCode) {
            this.crcCode = crcCode;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public long getSessionID() {
            return sessionID;
        }

        public void setSessionID(long sessionID) {
            this.sessionID = sessionID;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public byte getPriority() {
            return priority;
        }

        public void setPriority(byte priority) {
            this.priority = priority;
        }

        public Map<String, Object> getAttachment() {
            return attachment;
        }

        public void setAttachment(Map<String, Object> attachment) {
            this.attachment = attachment;
        }
    }
}