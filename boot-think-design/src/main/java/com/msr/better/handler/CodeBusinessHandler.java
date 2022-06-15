package com.msr.better.handler;

public interface CodeBusinessHandler {
    default boolean validate() {
        return true;
    }

    Object calculatePrice();
}
