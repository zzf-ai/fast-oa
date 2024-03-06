package com.zzf.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.process.ProcessType;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-31
 */
public interface OaProcessTypeService extends IService<ProcessType> {
    //查询所有审批分类和每个分类所有审批模板
    List<ProcessType> findProcessType();
}
