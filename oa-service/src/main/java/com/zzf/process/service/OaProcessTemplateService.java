package com.zzf.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.process.ProcessTemplate;

/**
 * @author zzf
 * @date 2024-01-31
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {
    //分页查询审批模板，把审批类型对应名称查询
    IPage<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam);

    //部署流程定义（发布）
    void publish(Long id);
}
