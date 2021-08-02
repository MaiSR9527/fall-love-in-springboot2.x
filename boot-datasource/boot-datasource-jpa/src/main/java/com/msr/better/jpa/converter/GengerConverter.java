package com.msr.better.jpa.converter;

import com.msr.better.jpa.constants.GenderEnum;

import javax.persistence.AttributeConverter;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
public class GengerConverter implements AttributeConverter<GenderEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(GenderEnum genderEnum) {
        return genderEnum.getId();
    }

    @Override
    public GenderEnum convertToEntityAttribute(Integer id) {
        return GenderEnum.getSexById(id);
    }
}
