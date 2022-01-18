package com.general.tree;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 描述:
 *
 * @author xvanning
 * @create 2022-01-18 22:54
 */
public class BuildTreeUtils {

    /**
     * 递归方法
     *
     * @param allCategoryNodeList 所有的类目
     * @param rootCategoryNode    根类目
     * @return 构造完毕的根类目
     */
    public static CategoryNode buildCategoryTreeDFS(List<CategoryNode> allCategoryNodeList, CategoryNode rootCategoryNode) {

        if (allCategoryNodeList.size() == 0) {
            return rootCategoryNode;
        }

        // 把每个节点加入到当前的 rootTree 中
        for (CategoryNode categoryNode : allCategoryNodeList) {
            List<CategoryNode> children = rootCategoryNode.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            if (Objects.equals(categoryNode.getParentId(), rootCategoryNode.getId())) {
                children.add(categoryNode);
            }
            rootCategoryNode.setChildren(children);
        }

        if (rootCategoryNode.getChildren() == null || rootCategoryNode.getChildren().size() == 0) {
            return rootCategoryNode;
        }

        // 继续子节点的构建
        for (CategoryNode child : rootCategoryNode.getChildren()) {
            buildCategoryTreeDFS(allCategoryNodeList, child);
        }

        return rootCategoryNode;
    }

    public static CategoryNode buildCategoryTreeBFS(List<CategoryNode> allCategoryNodeList, CategoryNode rootCategoryNode) {


        // 把每个节点加入到当前的 rootTree 中
        for (CategoryNode categoryNode : allCategoryNodeList) {
            List<CategoryNode> children = rootCategoryNode.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            if (Objects.equals(categoryNode.getParentId(), rootCategoryNode.getId())) {
                children.add(categoryNode);
            }
            rootCategoryNode.setChildren(children);
        }

        Queue<CategoryNode> queue = new LinkedList<>();
        queue.offer(rootCategoryNode);

        while (!queue.isEmpty()) {
            int sz = queue.size();
            /* 将当前队列中的所有节点向四周扩散 */
            for (int i = 0; i < sz; i++) {
                CategoryNode cur = queue.poll();
                List<CategoryNode> childrenNodes = cur.getChildren();

                if (CollectionUtils.isEmpty(childrenNodes)) {
                    continue;
                }

                for (CategoryNode curChildNode : childrenNodes) {

                    // 把每个节点加入到当前的 rootTree 中
                    for (CategoryNode categoryNode : allCategoryNodeList) {
                        List<CategoryNode> children = curChildNode.getChildren();
                        if (children == null) {
                            children = new ArrayList<>();
                        }
                        if (Objects.equals(categoryNode.getParentId(), curChildNode.getId())) {
                            children.add(categoryNode);
                        }
                        curChildNode.setChildren(children);
                    }

                    queue.offer(curChildNode);

                }
            }

        }

        return rootCategoryNode;
    }

    public static CategoryNode buildCategoryTreeSelf(List<CategoryNode> allCategoryNodeList, CategoryNode rootCategoryNode) {

        // 把每个节点加入到当前的 rootTree 中
        for (CategoryNode categoryNode : allCategoryNodeList) {
            List<CategoryNode> children = rootCategoryNode.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            if (Objects.equals(categoryNode.getParentId(), rootCategoryNode.getId())) {
                children.add(categoryNode);
            }
            rootCategoryNode.setChildren(children);
        }
        List<CategoryNode> children = rootCategoryNode.getChildren();
        while (!CollectionUtils.isEmpty(children)) {
            List<CategoryNode> nextChildList = new ArrayList<>();
            for (CategoryNode curChild : children) {
                // 把每个节点加入到当前的 rootTree 中
                for (CategoryNode categoryNode : allCategoryNodeList) {
                    List<CategoryNode> nextChild = curChild.getChildren();
                    if (nextChild == null) {
                        nextChild = new ArrayList<>();
                    }
                    if (Objects.equals(categoryNode.getParentId(), curChild.getId())) {
                        nextChild.add(categoryNode);
                    }
                    curChild.setChildren(nextChild);
                }
                nextChildList.addAll(curChild.getChildren());

            }

            children = nextChildList;

        }


        return rootCategoryNode;
    }

    public static void main(String[] args) {
        CategoryNode categoryNode1 = new CategoryNode(1L, "一级节点--1", 0L);
        CategoryNode categoryNode2 = new CategoryNode(2L, "一级节点--2", 0L);
        CategoryNode categoryNode3 = new CategoryNode(3L, "二级节点--3", 1L);
        CategoryNode categoryNode4 = new CategoryNode(4L, "二级节点--4", 1L);
        CategoryNode categoryNode5 = new CategoryNode(5L, "三级节点--5", 3L);
        CategoryNode categoryNode6 = new CategoryNode(6L, "三级节点--6", 3L);
        List<CategoryNode> allCategoryNodeList = new ArrayList<>();
        allCategoryNodeList.add(categoryNode1);
        allCategoryNodeList.add(categoryNode2);
        allCategoryNodeList.add(categoryNode3);
        allCategoryNodeList.add(categoryNode4);
        allCategoryNodeList.add(categoryNode5);
        allCategoryNodeList.add(categoryNode6);
        Kryo kryo = new Kryo();
        List<CategoryNode> copyAllNodeList = kryo.copy(allCategoryNodeList);

        CategoryNode categoryNode00 = new CategoryNode(0L, "根节点", null);
        CategoryNode categoryNode01 = new CategoryNode(0L, "根节点", null);


        CategoryNode categoryNodeDFS = BuildTreeUtils.buildCategoryTreeDFS(allCategoryNodeList, categoryNode00);
        System.out.println(JSON.toJSONString(categoryNodeDFS));


//        CategoryNode categoryNodeBFS = BuildTreeUtils.buildCategoryTreeBFS(copyAllNodeList, categoryNode01);
//        System.out.println(JSON.toJSONString(categoryNodeBFS));

        CategoryNode categoryNodeSelf = BuildTreeUtils.buildCategoryTreeSelf(copyAllNodeList, categoryNode01);
        System.out.println(JSON.toJSONString(categoryNodeSelf));

        System.out.println(JSON.toJSONString(categoryNodeDFS).equals(JSON.toJSONString(categoryNodeSelf)));



    }

}