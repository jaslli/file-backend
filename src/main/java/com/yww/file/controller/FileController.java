package com.yww.file.controller;

import com.yww.file.common.Result;
import com.yww.file.entity.SysFile;
import com.yww.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;

/**
 * <p>
 *      文件操作
 * </p>
 *
 * @author yww
 */
@Validated
@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService service;

    @Autowired
    public FileController(FileService service) {
        this.service = service;
    }

    /**
     * 普通地上传文件并保存
     */
    @PostMapping("saveFile")
    public Result<SysFile> saveFile(@RequestPart MultipartFile file) {
        return Result.success(service.saveFile(file));
    }

    /**
     * 通过文件ID更新文件
     */
    @PutMapping ("/updateFile/{fileId}")
    public Result<SysFile> updateFile(@Min(value = 0, message = "文件ID异常") @PathVariable Integer fileId, @RequestPart MultipartFile file) {
        return service.updateFile(fileId, file);
    }

    /**
     * 通过文件ID下载文件
     *
     * @param fileId    文件ID
     * @return          文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@Min(value = 0, message = "文件ID异常") @PathVariable Integer fileId) throws IOException {
        return service.download(fileId);
    }

    /**
     * 文件秒传
     */
    @PostMapping("/deduplication")
    public Result<SysFile> deduplication(@RequestPart MultipartFile file) {
        return Result.success(service.deduplication(file));
    }

}
