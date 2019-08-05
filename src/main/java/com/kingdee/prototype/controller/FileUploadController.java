package com.kingdee.prototype.controller;


import com.kingdee.prototype.base.enums.RetCode;
import com.kingdee.prototype.base.protocol.SimpleOutput;
import com.kingdee.prototype.cache.ProtoTypeHtmlCache;
import com.kingdee.prototype.model.PrototypeHtml;
import com.kingdee.prototype.util.UnzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/service/prototype/file")
@Slf4j
public class FileUploadController {

    @Autowired
    private ProtoTypeHtmlCache protoTypeHtmlCache;

    /**
     * 头像图片的放置路径
     */
    @Value("${kd.zipPath.protoType}")
    private String zipDir;

    /**
     * 获得文件存储路径
     *
     * @return
     */
    public String getZipDir() {
        return zipDir;
    }

    @RequestMapping("/upload")
    public SimpleOutput upload(@RequestParam("key") String key,
                               @RequestParam("name") String name,
                               @RequestParam("creator") String creator, @RequestParam("file_zip") MultipartFile file) {
        SimpleOutput output = null;
        try {
            if (file == null) {
                output = new SimpleOutput(RetCode.FAIL.retCode, "上传文件不能为空!", null);
            }
            UnzipUtil.unZipFiles(getZipDir(), key, file);
            PrototypeHtml prototypeHtml = protoTypeHtmlCache.get(key);
            if (prototypeHtml == null) {
                //新增
                PrototypeHtml prototypeHtmlTemp = new PrototypeHtml();
                prototypeHtmlTemp.setCreateTime(new Date());
                prototypeHtmlTemp.setUpdateTime(new Date());
                prototypeHtmlTemp.setKey(key);
                prototypeHtmlTemp.setName(name);
                prototypeHtmlTemp.setCreator(creator);
                prototypeHtml=new PrototypeHtml();
                BeanUtils.copyProperties(prototypeHtmlTemp,prototypeHtml);
            } else {
                prototypeHtml.setUpdateTime(new Date());
            }
            protoTypeHtmlCache.save(key,prototypeHtml);
            output = new SimpleOutput(RetCode.SUCCESS.retCode, RetCode.SUCCESS.message, null);
        } catch (Exception e) {
            log.error("上传文件失败 key:{} name:{} creator:{} e:{}", key, name, creator, e);
            output = new SimpleOutput(RetCode.FAIL.retCode, "上传文件失败！", null);
        }
        return output;
    }
}
