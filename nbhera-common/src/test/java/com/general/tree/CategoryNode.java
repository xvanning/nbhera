package com.general.tree;

import java.util.List;

/**
 * 描述:
 *
 * @author xvanning
 * @create 2022-01-18 22:55
 */

public class CategoryNode {

    private Long id;

    private String name;

    private Long parentId;

    private List<CategoryNode> children;

    public CategoryNode(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryNode() {

    }

    public List<CategoryNode> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryNode> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}