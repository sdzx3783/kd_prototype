package com.kingdee.prototype.model;

import lombok.Data;

import java.util.Date;

@Data
public class PrototypeHtml {

    /**
     * 原型key
     */
    private String key;
    /**
     * 原型name
     */
    private String name;

    /**
     * 原型url
     */
    private String path;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
