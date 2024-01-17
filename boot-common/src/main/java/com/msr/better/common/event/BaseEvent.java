package com.msr.better.common.event;

import lombok.Data;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * @date: 2024-01-17
 * @author: maisrcn@qq.com
 */
@Data
public class BaseEvent <T> implements ResolvableTypeProvider {
    private T event;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getEvent()));
    }
}
