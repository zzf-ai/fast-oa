package com.zzf.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.model.process.Process;
import com.zzf.model.vo.process.ProcessQueryVo;
import com.zzf.model.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Mapper
public interface OaProcessMapper extends BaseMapper<Process> {
    //审批管理列表
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, @Param("vo") ProcessQueryVo processQueryVo);
}
