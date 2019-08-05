package com.kingdee.prototype.util;

import com.kingdee.prototype.base.enums.RetCode;
import com.kingdee.prototype.base.protocol.SimpleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description:解压工具类
 * @Author: sunzhao
 * @Create on: 2019-08-04 14:11
 */
@Slf4j
public class UnzipUtil {


    public static SimpleOutput unZipFiles(String unZipPath, String fileName, MultipartFile multipartFile, boolean flag) throws IOException {
        log.debug("unZipPath：{} fileName:{}", unZipPath, fileName);
        SimpleOutput fileHandleResponse = null;
        String unZipRealPath = "";
        if (flag) {
            unZipRealPath = unZipPath.replaceAll("-", "") + "/";
        } else {
            unZipRealPath = unZipPath.replaceAll("-", "") + "/" + fileName + "/";
        }
        //如果保存解压缩文件的目录不存在，则进行创建，并且解压缩后的文件总是放在以fileName命名的文件夹下
        log.debug("unZipRealPath:{}", unZipRealPath);
        File unZipFile = new File(unZipRealPath);
        if (!unZipFile.exists()) {
            unZipFile.mkdirs();
        }
        ZipInputStream zipInputStream = null;
        try {
            //ZipInputStream用来读取压缩文件的输入流
            Charset gbk = Charset.forName("gbk");
            zipInputStream = new ZipInputStream(multipartFile.getInputStream(), gbk);
            //压缩文档中每一个项为一个zipEntry对象，可以通过getNextEntry方法获得，zipEntry可以是文件，也可以是路径，比如abc/test/路径下
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryName = zipEntry.getName();
                //将目录中的1个或者多个\置换为/，因为在windows目录下，以\或者\\为文件目录分隔符，linux却是/
                String outPath = (unZipRealPath + zipEntryName).replaceAll("\\+", "/");
                //判断所要添加的文件所在路径或者
                // 所要添加的路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是,在上面三行已经创建,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(outPath);
                    byte[] bytes = new byte[4096];
                    int len;
                    //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
                    while ((len = zipInputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, len);
                    }
                } catch (Exception e) {
                    log.error("解压文件发生异常 e:{}", e);
                } finally {
                    outputStream.close();
                    //必须调用closeEntry()方法来读入下一项
                    zipInputStream.closeEntry();
                }

            }
            fileHandleResponse = new SimpleOutput(RetCode.SUCCESS.retCode, RetCode.SUCCESS.message, null);
            System.out.println("******************解压完毕********************");

        } catch (Exception e) {
            log.error("解压文件发生异常 e:{}", e);
            fileHandleResponse = new SimpleOutput(RetCode.FAIL.retCode, RetCode.FAIL.message, null);
        } finally {
            if (zipInputStream != null) {
                zipInputStream.close();
            }
        }
        return fileHandleResponse;
    }

}
