package com.kingdee.prototype.controller;


import com.kingdee.prototype.base.enums.RetCode;
import com.kingdee.prototype.base.protocol.SimpleOutput;
import com.kingdee.prototype.cache.ProtoTypeHtmlCache;
import com.kingdee.prototype.model.PrototypeHtml;
import com.kingdee.prototype.util.RedisLockUtil;
import com.kingdee.prototype.util.RetCodeUtil;
import com.kingdee.prototype.util.UnzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
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


    private static final String PREX_DIRECTORY = "kd_EC_V_";

    private static final int FIRST_VERSION = 1;

    private static final String UPDATE_PROTOTYPE_KEY = "UPDATE_PROTOTYPE_KEY_";

    private static final int UPDATE_PROTOTYPE_TIMEOUT = 120000;

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
                               @RequestParam("creator") String creator,
                               @RequestParam(required = false, name = "remark") String remark,
                               @RequestParam(required = false, name = "file_zip") MultipartFile file) {
        SimpleOutput output = null;
        RLock rLock = null;
        boolean empty = file.isEmpty();
        try {
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("key参数不合法！");
            }
            rLock = RedisLockUtil.getLock(UPDATE_PROTOTYPE_KEY + key, UPDATE_PROTOTYPE_TIMEOUT);
            PrototypeHtml prototypeHtml = protoTypeHtmlCache.get(key);
            if (prototypeHtml == null && empty) {
                throw new RuntimeException("参数不合法！");
            }
            if (!empty) {
                /*if (prototypeHtml != null) {*/
                SimpleOutput output1 = UnzipUtil.unZipFiles(getZipDir() + "/" + key + "/"
                                + PREX_DIRECTORY
                                + (prototypeHtml == null ? FIRST_VERSION : (prototypeHtml.getVersion().intValue() + 1))
                        , key, file, true);
                if (!RetCodeUtil.isSucc(output1.getRetCode())) {
                    throw new RuntimeException("上传文件失败！");
                }
                /*}*/
                SimpleOutput output2 = UnzipUtil.unZipFiles(getZipDir(), key, file, false);
                if (!RetCodeUtil.isSucc(output2.getRetCode())) {
                    throw new RuntimeException("上传文件失败！");
                }
            }
            if (prototypeHtml == null) {
                //新增
                PrototypeHtml prototypeHtmlTemp = new PrototypeHtml();
                prototypeHtmlTemp.setVersion(FIRST_VERSION);
                prototypeHtmlTemp.setCreateTime(new Date());
                prototypeHtmlTemp.setUpdateTime(new Date());
                prototypeHtmlTemp.setKey(key);
                prototypeHtmlTemp.setName(name);
                prototypeHtmlTemp.setCreator(creator);
                prototypeHtmlTemp.setRemark(remark);
                prototypeHtml = new PrototypeHtml();
                BeanUtils.copyProperties(prototypeHtmlTemp, prototypeHtml);
            } else {
                if (!empty) {
                    protoTypeHtmlCache.saveUpdateLog(key, prototypeHtml);
                }
                if (!empty) {
                    //zip包更新才更新时间
                    prototypeHtml.setUpdateTime(new Date());
                    prototypeHtml.setVersion(prototypeHtml.getVersion() + 1);
                }
                prototypeHtml.setCreator(creator);
                prototypeHtml.setName(name);
                prototypeHtml.setRemark(remark);
            }
            protoTypeHtmlCache.save(key, prototypeHtml);
            output = new SimpleOutput(RetCode.SUCCESS.retCode, RetCode.SUCCESS.message, null);
        } catch (Exception e) {
            log.error("上传文件失败 key:{} name:{} creator:{} e:{}", key, name, creator, e);
            output = new SimpleOutput(RetCode.FAIL.retCode, "操作失败！", null);
        } finally {
            if (rLock != null) {
                rLock.unlock();
            }
        }
        return output;
    }
}
