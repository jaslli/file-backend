package com.yww.filebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.filebackend.entity.SysFile;
import com.yww.filebackend.mapper.SysFileMapper;
import com.yww.filebackend.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      文件信息实体类 服务实现类
 * </p>
 *
 * @author yww
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {
}
