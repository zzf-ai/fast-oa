package com.zzf.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.model.process.ProcessTemplate;
import com.zzf.process.mapper.OaProcessTemplateMapper;
import com.zzf.process.service.OaProcessService;
import com.zzf.process.service.OaProcessTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {


    @Autowired
    private OaProcessService processService;

    @Override
    public IPage<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam) {

        IPage<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam);
        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        //修改模板发布状态 1 已经发布
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);
        //流程定义部署
        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
