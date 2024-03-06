package com.zzf.process.service;

/**
 * @author zzf
 * @date 2024-02-13
 */
public interface MessageService {
    /**
     * 推送待审批人员
     * @param processId 当前流程id
     * @param userId 待审批人员id
     * @param taskId 任务id
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);

    /**
     * 审批后推送提交审批人员
     * @param processId
     * @param userId
     * @param status
     */
    void pushProcessedMessage(Long processId, Long userId, Integer status);

}
