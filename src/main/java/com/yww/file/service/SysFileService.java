package com.yww.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.file.entity.SysFile;

/**
 * <p>
 *      文件信息实体类 服务类
 * </p>
 *
 * @author yww
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 通过hash值查询文件
     *
     * @param hash  hash值
     * @return      文件信息对象
     */
    SysFile selectByHash(String hash);

}
