package com.msr.better.jpa.constants;

import lombok.Getter;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
@Getter
public enum GenderEnum {
    /**
     * 1为男
     */
    MALE(1,"男"),
    /**
     * 2为女
     */
    FEMALE(2,"女");
    private Integer id;
    private String name;

    GenderEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GenderEnum getSexById(int id){
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getId()==id){
                return genderEnum;
            }
        }
        return null;
    }

}
