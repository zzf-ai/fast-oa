package com.zzf.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.auth.service.SysUserService;
import com.zzf.model.process.ProcessRecord;
import com.zzf.model.system.SysUser;
import com.zzf.process.mapper.OaProcessRecordMapper;
import com.zzf.process.service.OaProcessRecordService;
import com.zzf.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzf
 * @date 2024-02-03
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    //记录审批信息
    @Override
    public void record(Long processId, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(sysUser.getName());
        processRecord.setOperateUserId(userId);
        baseMapper.insert(processRecord);
    }
}
