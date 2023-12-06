package com.msr.better.common.datastruct;

import java.util.StringJoiner;

/**
 * 三叉搜索前缀树
 *
 * @date: 2023-12-06
 * @author: maisrcn@qq.com
 */
public final class TSTNode {
    public Object data;

    /**
     * 左节点
     */
    protected TSTNode loNode;
    /**
     * 中间节点
     */
    protected TSTNode eqNode;
    /**
     * 右节点
     */
    protected TSTNode hiNode;
    /**
     * 本节点表示的字符
     */
    protected char splitChar;

    public TSTNode(char splitChar) {
        this.splitChar = splitChar;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TSTNode.class.getSimpleName() + "{", "}")
                .add("splitChar=" + splitChar)
                .toString();
    }

    protected TSTNode getNode(String key, TSTNode startNode) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        TSTNode currentNode = startNode;
        int charIndex = 0;
        char compareChar = key.charAt(charIndex);
        int compareIndex;
        while (true) {
            if (currentNode == null) {
                return null;
            }
            compareIndex = compareChar - currentNode.splitChar;
            if (compareIndex == 0) {
                charIndex++;
                if (key.length() == charIndex) {
                    return currentNode;
                } else {
                    compareChar = key.charAt(charIndex);
                }
                currentNode = currentNode.eqNode;
            } else if (compareIndex < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
    }
}
