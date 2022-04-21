package com.msr.better.redis.model;

import lombok.Data;

import java.util.Map;

@Data
public class Message {

    private Integer messageType;

    private Long ownerId;

    private String messageContent;

    private Map<String,String> additionalInformation;
}
