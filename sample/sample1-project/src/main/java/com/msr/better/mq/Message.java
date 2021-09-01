package com.msr.better.mq;

import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:13:53
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -8657613687306891080L;
    private String key;
    private Object content;
    private Integer failTimes;

    public Message(String key, Object content) {
        this.key = key;
        this.content = content;
        this.failTimes = 0;
    }

    public Message() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getFailTimes() {
        return this.failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Message [key=");
        builder.append(this.key);
        builder.append(", content=");
        builder.append(this.content);
        builder.append(", failTimes=");
        builder.append(this.failTimes);
        builder.append("]");
        return builder.toString();
    }
}
