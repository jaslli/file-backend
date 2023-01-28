package com.yww.filebackend.controller;

import com.yww.filebackend.common.Result;
import com.yww.filebackend.entity.SysFile;
import com.yww.filebackend.service.SysFileService;
import com.yww.filebackend.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *      文件上传
 * </p>
 *
 * @author yww
 */
@RestController
@CrossOrigin
@RequestMapping("/upload")
public class FileUploadController {

    private final SysFileService service;

    @Autowired
    public FileUploadController(SysFileService service) {
        this.service = service;
    }

    /**
     * 上传文件并保存
     */
    @PostMapping("saveFile")
    public Result<SysFile> getById(MultipartFile file) {
        if (file == null) {
            return Result.failure("文件不能为空");
        }
        SysFile sysFile = FileUtil.saveFile(file);
        service.save(sysFile);
        return Result.success(sysFile);
    }

}
