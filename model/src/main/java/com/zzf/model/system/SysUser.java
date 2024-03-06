package com.zzf.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzf.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-26
 */
@Data
@Schema(description = "用户")
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "手机")
    @TableField("phone")
    private String phone;

    @Schema(description = "头像地址")
    @TableField("head_url")
    private String headUrl;

    @Schema(description = "部门id")
    @TableField("dept_id")
    private Long deptId;

    @Schema(description = "岗位id")
    @TableField("post_id")
    private Long postId;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "openId")
    @TableField("open_id")
    private String openId;

    @Schema(description = "状态（1：正常 0：停用）")
    @TableField("status")
    private Integer status;

    @Schema(description = "角色列表")
    @TableField(exist = false)
    private List<SysRole> roleList;

    @Schema(description = "岗位")
    @TableField(exist = false)
    private String postName;

    @Schema(description = "部门")
    @TableField(exist = false)
    private String deptName;
}
