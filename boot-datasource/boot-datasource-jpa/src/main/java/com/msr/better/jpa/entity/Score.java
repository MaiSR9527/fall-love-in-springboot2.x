package com.msr.better.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-11-28 23:21
 **/
@Data
@Entity
@Table(name = "t_score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;
    private Long stuId;
    private Long courseId;
    private Date createTime;
    private Date updateTime;
}
