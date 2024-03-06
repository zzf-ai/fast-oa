package com.zzf.model.vo.process;

import lombok.Data;

/**
 * @author zzf
 * @date 2024-02-03
 */
@Data
public class ApprovalVo {

    private Long processId;

    private String taskId;

    //状态
    private Integer status;

    //审批描述
    private String description;
}
