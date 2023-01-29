package com.yww.filebackend.controller;

import com.yww.filebackend.common.Result;
import com.yww.filebackend.entity.SysFile;
import com.yww.filebackend.service.SysFileService;
import com.yww.filebackend.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *      文件信息实体类 前端控制器
 * </p>
 *
 * @author yww
 */
@RestController
@CrossOrigin
@RequestMapping("/file")
public class SysFileController {

    private final SysFileService service;

    @Autowired
    public SysFileController(SysFileService service) {
        this.service = service;
    }

    /**
     * 根据ID获取文件信息
     */
    @GetMapping("/getById/{id}")
    public Result<SysFile> getById(@PathVariable Integer id) {
        AssertUtil.notNull(id, "文件ID不能为空！");
        return Result.success(service.getById(id));
    }

    /**
     * 获取所有文件信息
     */
    @GetMapping("/getAll")
    public Result<List<SysFile>> getAll() {
        return Result.success(service.list());
    }

    @DeleteMapping("/deleteById/{id}")
    public Result<String> deleteById(@PathVariable Integer id) {
        AssertUtil.notNull(id, "文件ID不能为空！");
        if (service.removeById(id)) {
            return Result.success("删除成功！");
        } else {
            return Result.failure("删除失败！");
        }
    }

}
