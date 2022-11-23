package com.msr.better.jpa.entity;

import com.msr.better.jpa.constants.GenderEnum;
import com.msr.better.jpa.converter.GenderConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    @Convert(converter = GenderConverter.class)
    private GenderEnum gender;
    private Integer age;
    private String education;
    private Integer status;
    private Integer enableStatus;
    private String position;
    private String idCardNumber;
    private Date createTime;
    private String creator;
    private Date updateTime;
    private String modifier;
    private String nickName;
    private String iconPath;
}
