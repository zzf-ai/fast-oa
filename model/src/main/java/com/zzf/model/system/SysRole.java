package com.zzf.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzf.model.base.BaseEntity;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-01-25
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //角色名称
    @TableField("role_name")
    private String roleName;

    //角色编码
    @TableField("role_code")
    private String roleCode;

    //描述
    @TableField("description")
    private String description;
}
