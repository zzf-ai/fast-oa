package com.zzf.model.vo.process;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Data
@Schema(description = "Process")
public class ProcessVo {

    private Long id;

    private Date createTime;

    @Schema(description = "审批code")
    private String processCode;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "process_template_id")
    private Long processTemplateId;

    @Schema(description = "审批模板")
    private String processTemplateName;

    @Schema(description = "审批类型id")
    private Long processTypeId;

    @Schema(description = "审批类型")
    private String processTypeName;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "表单属性")
    private String formProps;

    @Schema(description = "表单选项")
    private String formOptions;

    @Schema(description = "表单属性值")
    private String formValues;

    @Schema(description = "流程实例id")
    private String processInstanceId;

    @Schema(description = "当前审批人")
    private String currentAuditor;

    @Schema(description = "状态（0：默认 1：审批中 2：审批通过 -1：驳回）")
    private Integer status;

    @Schema(description = "流程任务id")
    private String taskId;

}
