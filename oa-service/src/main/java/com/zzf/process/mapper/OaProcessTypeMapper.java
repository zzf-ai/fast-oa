package com.zzf.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzf.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Mapper
public interface OaProcessTypeMapper extends BaseMapper<ProcessType> {
    List<ProcessType> selectProcessTypeAndTemplatesList();
}
