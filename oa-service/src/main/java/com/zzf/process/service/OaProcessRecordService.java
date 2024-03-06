package com.zzf.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.process.ProcessRecord;

/**
 * @author zzf
 * @date 2024-02-03
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId, Integer status, String description);
}
