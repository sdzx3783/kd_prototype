package com.kingdee.prototype.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 原型
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
