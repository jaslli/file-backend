package com.yww.file.service;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.minio.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *      minio配置
 * </P>
 *
 * @author yww
 * @since 2023/6/3
 */
public class MinioServer {

    public static MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint("http://192.168.2.23:9000")
                .credentials("Dv9qol7QORbBFbSw1MPJ", "2CSe5wTysAjXUJ6Gr3AQTsW5BtJ9V80YpXRUpqsm")
                .build();
    }

    public static void upload(String filePath, String fileName, String bucketName) throws Exception {
        MinioClient minioClient = getMinioClient();
        // 判断存储桶是否存在
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        File file = FileUtil.file(filePath);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(FileUtil.getInputStream(file), FileUtil.size(file), -1).build();
        minioClient.putObject(putObjectArgs);
    }


    /**
     * 下载文件
     */
    public void fileDownload(String fileName,
                             String bucketName,
                             HttpServletResponse response) {
        InputStream inputStream   = null;
        OutputStream outputStream = null;
        try {
            if (StringUtils.isBlank(fileName)) {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                String data = "文件下载失败";
                OutputStream ps = response.getOutputStream();
                ps.write(data.getBytes(StandardCharsets.UTF_8));
                return;
            }
            MinioClient minioClient = getMinioClient();
            outputStream = response.getOutputStream();
            // 获取文件对象
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            byte[] buf = new byte[1024];
            int length;
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName.substring(fileName.lastIndexOf("/") + 1), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            // 输出文件
            while ((length = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            System.out.println("下载成功");
            inputStream.close();
        } catch (Throwable ex) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下载失败";
            try {
                OutputStream ps = response.getOutputStream();
                ps.write(data.getBytes(StandardCharsets.UTF_8));
            }catch (IOException e){
                e.printStackTrace();
            }
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }}catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
