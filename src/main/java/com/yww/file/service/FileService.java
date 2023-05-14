package com.yww.file.service;

import com.yww.file.common.BusinessException;
import com.yww.file.common.Result;
import com.yww.file.entity.SysFile;
import com.yww.file.utils.AssertUtil;
import com.yww.file.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 *      文件服务类
 * </P>
 *
 * @author yww
 * @since 2023/5/14
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class FileService {

    private final SysFileService fileService;

    /**
     * 文件保存路径（需要注意）
     */
    @Value("{file.save-path}")
    private static String savePath;

    public SysFile saveFile(MultipartFile file) {
        // 计算哈希值
        String hash;
        try {
            byte[] bytes = file.getBytes();
            hash = FileUtil.getSha256(bytes);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
        // 保存文件
        return saveFileByHash(hash, file);
    }

    public SysFile saveFileByHash(String hash, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String path = FileUtil.getSavePath(savePath, originalFilename);
        File dest = new File(path);
        // 若是路径不存在就先创建父级目录
        if (!dest.exists()) {
            cn.hutool.core.io.FileUtil.touch(dest);
        }
        // 保存文件
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("保存文件出错，保存的文件名称为：" + originalFilename);
            log.error("保存文件出错，保存的路径为：" + path);
            throw new BusinessException(e.getMessage());
        }
        // 保存记录并返回
        SysFile resFile = SysFile.builder()
                .fileName(originalFilename)
                .path(path)
                .size(file.getSize())
                .hash(hash)
                .build();
        fileService.save(resFile);
        return resFile;
    }

    public Result<SysFile> updateFile(Integer fileId, MultipartFile file) {
        // 保存新文件
        SysFile newFile;
        try {
            newFile = saveFile(file);
        } catch (BusinessException e) {
            return Result.failure(e.getMessage());
        }

        // 计算哈希值
        String hash;
        try {
            byte[] bytes = file.getBytes();
            hash = FileUtil.getSha256(bytes);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }

        // 更新文件记录
        SysFile sysFile = fileService.getById(fileId);
        sysFile.setFileName(newFile.getFileName());
        sysFile.setPath(newFile.getPath());
        sysFile.setSize(newFile.getSize());
        sysFile.setHash(hash);

        if (fileService.updateById(sysFile)) {
            return Result.success(sysFile);
        } else {
            return Result.failure();
        }

    }

    public ResponseEntity<byte[]> download(Integer fileId) throws IOException {
        // 查询文件
        SysFile sysFile = fileService.getById(fileId);
        AssertUtil.notNull(sysFile, "找不到对应的文件！");

        // 获取文件
        Path filePath = Paths.get("path", "to", "file").toAbsolutePath();
        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            throw new FileNotFoundException();
        }
        File file = filePath.toFile();

        // 转换为字节
        byte[] bytes = Files.readAllBytes(file.toPath());

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        // 标识数据类型（application/octet-stream）
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 设置浏览器如何处理响应体的内容
        // inline表示浏览器应该直接显示响应体的内容，而attachment表示浏览器应该提示用户将响应体内容保存到本地
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
        // 设置文件数据长度
        headers.setContentLength(bytes.length);

        // 返回文件
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    public SysFile deduplication(String fileName, String hash) {
        // 查询hash值是否存在
        SysFile sysFile = fileService.selectByHash(hash);

        // 如果文件不存在，则返回空
        if (null == sysFile) {
            return null;
        }

        // 如果文件已经存在，那使用之前文件的路径返回
        SysFile resFile = SysFile.builder()
                .fileName(fileName)
                .path(sysFile.getPath())
                .size(sysFile.getSize())
                .hash(hash)
                .build();
        // 保存文件记录，并返回
        fileService.save(resFile);
        return resFile;
    }

}
