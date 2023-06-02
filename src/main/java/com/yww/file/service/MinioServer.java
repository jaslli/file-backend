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
    public static void fileDownload(String fileName, String bucketName, String savePath) throws Exception {
        MinioClient minioClient = getMinioClient();
        InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        FileUtil.writeFromStream(inputStream, FileUtil.file(savePath));
    }

}
