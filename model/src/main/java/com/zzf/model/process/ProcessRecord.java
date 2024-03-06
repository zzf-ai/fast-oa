package com.zzf.model.process;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzf.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-02-03
 */
@Data
@Schema(description = "ProcessRecord")
@TableName("oa_process_record")
public class ProcessRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "审批流程id")
    @TableField("process_id")
    private Long processId;

    @Schema(description = "审批描述")
    @TableField("description")
    private String description;

    @Schema(description = "状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "操作用户id")
    @TableField("operate_user_id")
    private Long operateUserId;

    @Schema(description = "操作用户")
    @TableField("operate_user")
    private String operateUser;

}
