package com.zzf.model.process;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzf.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Data
@Schema(description = "Process")
@TableName("oa_process")
public class Process extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "审批code")
    @TableField("process_code")
    private String processCode;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "审批模板id")
    @TableField("process_template_id")
    private Long processTemplateId;

    @Schema(description = "审批类型id")
    @TableField("process_type_id")
    private Long processTypeId;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "表单值")
    @TableField("form_values")
    private String formValues;

    @Schema(description = "流程实例id")
    @TableField("process_instance_id")
    private String processInstanceId;

    @Schema(description = "当前审批人")
    @TableField("current_auditor")
    private String currentAuditor;

    @Schema(description = "状态（0：默认 1：审批中 2：审批通过 -1：驳回）")
    @TableField("status")
    private Integer status;
}
