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
public class AssignMenuVo {

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "菜单id列表")
    private List<Long> menuIdList;

}
