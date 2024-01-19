package com.msr.better.common.util;

import com.google.common.collect.Lists;
import com.msr.better.common.datastruct.TreeNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @date: 2024-01-18
 * @author: maisrcn@qq.com
 */
public class Treeutil {

    public static List<TreeNode> flattenTreeToList(List<TreeNode> treeNodeList) {
        List<TreeNode> flattenedList = Lists.newArrayList();
        for (TreeNode node : treeNodeList) {
            // 可以对 node 的一些属性进行过滤，不符合的不添加到 list
            flattenedList.add(node);
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                flattenedList.addAll(flattenTreeToList(node.getChildren()));
                // 清空节点的子节点列表
                node.getChildren().clear();
            }
        }
        return flattenedList;
    }
}
