package com.zzf.model.vo.process;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Data
@Schema(description = "Process")
public class ProcessQueryVo {
    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "process_template_id")
    private Long processTemplateId;

    @Schema(description = "审批类型id")
    private Long processTypeId;

    @Schema(description = "开始时间")
    private String createTimeBegin;

    @Schema(description = "结束时间")
    private String createTimeEnd;

    @Schema(description = "状态（0：默认 1：审批中 2：审批通过 -1：驳回）")
    private Integer status;
}
