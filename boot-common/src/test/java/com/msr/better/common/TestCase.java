package com.msr.better.common;

import com.beust.jcommander.internal.Lists;
import com.msr.better.common.datastruct.TreeNode;
import com.msr.better.common.util.Treeutil;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @date: 2024-01-18
 * @author: maisrcn@qq.com
 */
public class TestCase {


    @Test
    public void testFlattenTree() {
        TreeNode root = new TreeNode("root");

        TreeNode child1 = new TreeNode("child1");

        TreeNode child2 = new TreeNode("child2");

        TreeNode child3 = new TreeNode("child3");

        TreeNode child4 = new TreeNode("child4");

        child1.setChildren(Lists.newArrayList(child3));
        child2.setChildren(Lists.newArrayList(child4));

        root.setChildren(Lists.newArrayList(child1, child2));

        // List<TreeNode> treeNodesHasRoot = Treeutil.flattenTreeToList(Lists.newArrayList(root));
        // System.out.println(treeNodesHasRoot);

        List<TreeNode> treeNodesNoRoot = Treeutil.flattenTreeToList(root.getChildren());
        System.out.println(treeNodesNoRoot);
    }
}
