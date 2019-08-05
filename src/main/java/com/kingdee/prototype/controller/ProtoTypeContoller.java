package com.kingdee.prototype.controller;

import com.kingdee.prototype.base.enums.RetCode;
import com.kingdee.prototype.base.protocol.SimpleOutput;
import com.kingdee.prototype.cache.ProtoTypeHtmlCache;
import com.kingdee.prototype.model.PrototypeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: sunzhao
 * @Create on: 2019-08-04 17:30
 */
@RestController
@RequestMapping("/service/prototype/html")
public class ProtoTypeContoller {

    @Autowired
    private ProtoTypeHtmlCache protoTypeHtmlCache;

    @RequestMapping("getAll")
    public SimpleOutput<List<PrototypeHtml>> getProtocolTypes() {
        return new SimpleOutput(RetCode.SUCCESS.retCode, RetCode.SUCCESS.message, protoTypeHtmlCache.getAll());
    }
}
