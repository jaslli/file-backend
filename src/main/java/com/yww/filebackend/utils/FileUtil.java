package com.yww.filebackend.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yww.filebackend.entity.SysFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *      文件工具类
 * </p>
 *
 * @author yww
 */
@Slf4j
public class FileUtil {

    /**
     * 文件保存路径（需要注意）
     */
    private final static String FILE_SAVE_PATH = "D:\\file";

    /**
     * 文件日期部分路径格式
     */
    public final static String DATE_FORMAT = "/yyyy/MM/dd/";
    /**
     * windows下文件日期部分路径格式
     */
    private final static String DATE_FORMAT_WIN = "\\yyyy\\MM\\dd\\";

    /**
     * 保存文件
     *
     * @param file  文件
     * @return      文件信息
     */
    public static SysFile saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        // 获取文件扩展名
        String ext = getExtName(originalFilename);
        // 生成新的文件名称
        String newName = IdUtil.fastSimpleUUID() + "." + ext;
        String dateFormat = new SimpleDateFormat(getDateFormat()).format(new Date());
        String path = FILE_SAVE_PATH + dateFormat + newName;
        File dest = new File(path);
        // 若是路径不存在就先创建父级目录
        if (!dest.exists()) {
            cn.hutool.core.io.FileUtil.touch(dest);
        }
        // 保存文件
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.warn("保存文件出错，保存的文件名称为：" + originalFilename);
            log.warn("保存文件出错，保存的路径为：" + path);
            throw new RuntimeException(e);
        }
        return SysFile.builder()
                .fileName(originalFilename)
                .path(path)
                .size(file.getSize())
                .build();
    }

    /**
     * 获取文件后缀
     * 参考自hutool的FileUtil.extName()方法
     *
     * @param fileName  文件名称
     * @return          文件后缀
     */
    public static String getExtName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        // 可能会有两个.符号
        int secondToLastIndex = fileName.substring(0, index).lastIndexOf(".");
        return fileName.substring(secondToLastIndex == -1 ? index + 1 : secondToLastIndex + 1);
    }

    /**
     * 获取日期格式
     * 由于操作系统不一样，所以可能会有不同文件路径格式
     *
     * @return  日期格式
     */
    public static String getDateFormat() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (StrUtil.isNotBlank(osName) && osName.contains("win")) {
            return DATE_FORMAT_WIN;
        }
        return DATE_FORMAT;
    }

    /**
     * 计算文件MD5值
     *
     * @param file  文件
     * @return      文件的MD5值
     */
    public static StringBuilder getMd5ByFile(MultipartFile file) {
        StringBuilder res = new StringBuilder();
        try (InputStream inputStream = file.getInputStream()) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            md5.update(bytes);
            for (byte by : md5.digest()) {
                res.append(String.format("%02X", by));
            }
        } catch (NoSuchAlgorithmException | IOException exception) {
            log.warn("计算MD5过程出错");
        }
        return res;
    }

}
