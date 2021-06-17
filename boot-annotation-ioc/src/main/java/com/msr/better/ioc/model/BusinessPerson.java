package com.msr.better.ioc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Component
public class BusinessPerson implements Person{

    /**
     * 依赖注入
     */
    private final Animal animal;

    public BusinessPerson(@Qualifier("cat") Animal animal) {
        this.animal = animal;
    }

    @Override
    public void service() {
        this.animal.user();
    }
}
