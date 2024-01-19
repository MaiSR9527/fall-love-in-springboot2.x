package com.msr.better.common.datastruct;

import lombok.Data;

import java.util.List;

/**
 * @date: 2024-01-18
 * @author: maisrcn@qq.com
 */
@Data
public class TreeNode {

    private String name;

    private List<TreeNode> children;

    public TreeNode(String name) {
        this.name = name;
    }
}
