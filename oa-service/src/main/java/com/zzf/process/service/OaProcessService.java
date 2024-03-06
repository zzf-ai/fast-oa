package com.zzf.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.process.Process;
import com.zzf.model.vo.process.ApprovalVo;
import com.zzf.model.vo.process.ProcessFormVo;
import com.zzf.model.vo.process.ProcessQueryVo;
import com.zzf.model.vo.process.ProcessVo;

import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-31
 */
public interface OaProcessService extends IService<Process> {

    //部署流程定义
    void deployByZip(String deployPath);

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
