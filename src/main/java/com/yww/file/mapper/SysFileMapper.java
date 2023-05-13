package com.yww.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.file.entity.SysFile;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *      文件信息实体类 Mapper 接口
 * </p>
 *
 * @author chenhao
 */
@Repository
public interface SysFileMapper extends BaseMapper<SysFile> {

    /**
     * 通过hash值查询文件
     *
     * @param hash  hash值
     * @return      文件信息对象
     */
    SysFile selectByHash(String hash);

}
