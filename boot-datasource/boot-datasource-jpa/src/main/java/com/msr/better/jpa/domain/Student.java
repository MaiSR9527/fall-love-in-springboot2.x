package com.msr.better.jpa.domain;

import com.msr.better.jpa.constants.GenderEnum;
import com.msr.better.jpa.converter.GengerConverter;
import lombok.Data;

import javax.persistence.*;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
@Data
@Entity
@Table(name = "t_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Convert(converter = GengerConverter.class)
    private GenderEnum gender;
    private Integer age;
}
