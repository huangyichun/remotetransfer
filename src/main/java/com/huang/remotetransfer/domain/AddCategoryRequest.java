package com.huang.remotetransfer.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangyichun
 * @Date: 2021/3/24
 */
@Data
public class AddCategoryRequest implements Serializable {

    protected Long categoryId;

    protected String name;

    protected String nameEn;

    protected Integer visible;

    protected String tenantName;

    protected Integer bizid;

    protected Long parentId;
}
