package com.zzf.model.vo.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-26
 */
@Schema(description = "分配菜单")
@Data
public class AssignRoleVo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id列表")
    private List<Long> roleIdList;

}
