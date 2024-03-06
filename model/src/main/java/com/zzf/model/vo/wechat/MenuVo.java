package com.zzf.model.vo.wechat;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zzf
 * @date 2024-02-08
 */
@Data
@Schema(description = "菜单")
public class MenuVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "id")
    private Long parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "url")
    private String url;

    @Schema(description = "菜单key")
    private String meunKey;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "下级")
    @TableField(exist = false)
    private List<MenuVo> children;

}
