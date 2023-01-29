package com.yww.filebackend.controller;

import com.yww.filebackend.common.Result;
import com.yww.filebackend.entity.SysFile;
import com.yww.filebackend.service.SysFileService;
import com.yww.filebackend.utils.AssertUtil;
import com.yww.filebackend.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 *      文件操作
 * </p>
 *
 * @author yww
 */
@RestController
@CrossOrigin
public class FileController {

    private final SysFileService service;

    @Autowired
    public FileController(SysFileService service) {
        this.service = service;
    }

    /**
     * 上传文件并保存
     */
    @PostMapping("upload/saveFile")
    public Result<SysFile> getById(@RequestPart MultipartFile file) {
        AssertUtil.notNull(file, "文件不能为空");
        SysFile sysFile = FileUtil.saveFile(file);
        service.save(sysFile);
        return Result.success(sysFile);
    }

    /**
     * 通过文件ID更新文件
     */
    @PutMapping ("/updateById/{fileId}")
    public Result<SysFile> updateById(@PathVariable Integer fileId, @RequestPart MultipartFile file) {
        SysFile newFile = FileUtil.saveFile(file);
        SysFile sysFile = service.getById(fileId);
        sysFile.setFileName(newFile.getFileName());
        sysFile.setPath(newFile.getPath());
        sysFile.setSize(newFile.getSize());
        if (service.updateById(sysFile)) {
            return Result.success(sysFile);
        } else {
            return Result.failure();
        }
    }

    /**
     * 通过文件ID下载文件
     *
     * @param fileId    文件ID
     * @return          文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Integer fileId) {
        AssertUtil.notNull(fileId, "文件ID不能为空！");
        SysFile sysFile = service.getById(fileId);
        System.out.println(sysFile);
        AssertUtil.notNull(sysFile, "找不到对应的文件！");
        // Content-Disposition请求头
        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(new String(sysFile.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(sysFile.getPath()));
    }

}
