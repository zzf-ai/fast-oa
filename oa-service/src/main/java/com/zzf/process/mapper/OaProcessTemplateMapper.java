package com.zzf.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.model.process.ProcessTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Mapper
public interface OaProcessTemplateMapper extends BaseMapper<ProcessTemplate> {
    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);
}
