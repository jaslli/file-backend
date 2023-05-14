package com.yww.file.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yww.file.common.BusinessException;
import lombok.extern.slf4j.Slf4j;

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
     * 文件日期部分路径格式
     */
    public final static String DATE_FORMAT = "/yyyy/MM/dd/";

    /**
     * windows下文件日期部分路径格式
     */
    private final static String DATE_FORMAT_WIN = "\\yyyy\\MM\\dd\\";

    /**
     * 获取下载地址
     *
     * @param savePath  保存地址
     * @param fileName  文件名称
     * @return          文件保存地址
     */
    public static String getSavePath(String savePath, String fileName) {
        // 获取文件扩展名
        String ext = FileUtil.getExtName(fileName);
        // 生成新的文件名称
        String newName = IdUtil.fastSimpleUUID() + "." + ext;
        String dateFormat = new SimpleDateFormat(FileUtil.getDateFormat()).format(new Date());
        return savePath + dateFormat + newName;
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
     * 计算文件SHA-256的哈希值
     *
     * @param bytes 文件的字节数组
     * @return      十六进制哈希值
     */
    public static String getSha256(byte[] bytes) {
        // 获取SHA-256算法对象
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("获取SHA-256算法出错！");
            throw new BusinessException(e.getMessage());
        }
        // 将字节数组传入MessageDigest对象中
        md.update(bytes);
        // 计算哈希值并返回字节数组
        byte[] hash = md.digest();
        // 将哈希值转换为十六进制字符串并返回
        return bytesToHex(hash);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 文件的字节数组
     * @return      十六进制哈希值
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hex = "0123456789abcdef".toCharArray();
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xff;
            chars[i * 2] = hex[b >>> 4];
            chars[i * 2 + 1] = hex[b & 0x0f];
        }
        return new String(chars);
    }

}
