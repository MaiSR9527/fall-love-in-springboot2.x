package com.msr.better.bean;

import com.msr.better.annotation.ListValue;
import com.msr.better.validate.ValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-17
 */
@Data
public class Article {
    /**
     * 文章id
     */
    @NotNull(message = "文章更新时articleId不能为空", groups = {ValidateGroup.UpdateValidate.class})
    @Null(message = "文章新增时articleId必须为空", groups = {ValidateGroup.AddValidate.class})
    private Long articleId;
    /**
     * 文章标题
     */
//    @Size(min = 5, max = 10)
//    @NotBlank(message = "文章标题不能为空")
    @Size(min = 5, max = 10, groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    @NotBlank(message = "文章标题不能为空", groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 描述
     */
    private String description;
    /**
     * 内容
     */
    private String context;
    /**
     * 邮箱
     */
    @Email
    private String email;
    /**
     * 封面
     */
    @URL(groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    private String coverImage;

    /**
     * 状态
     */
    @ListValue(list = {0, 1, 2}, groups = {ValidateGroup.ArticleStatusValidate.class})
    private Integer status;
}
