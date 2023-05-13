package com.yww.file.controller;

import com.yww.file.common.Result;
import com.yww.file.entity.SysFile;
import com.yww.file.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p>
 *      文件信息实体类 前端控制器
 * </p>
 *
 * @author yww
 */
@Validated
@CrossOrigin
@RestController
@RequestMapping("/sysFile")
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
    public Result<SysFile> getById(@Min(value = 0, message = "文件ID异常") @PathVariable Integer id) {
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
    public Result<String> deleteById(@Min(value = 0, message = "文件ID异常") @PathVariable Integer id) {
        if (service.removeById(id)) {
            return Result.success("删除成功！");
        } else {
            return Result.failure("删除失败！");
        }
    }

}
