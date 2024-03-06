package com.zzf.model.wechat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzf.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-02-08
 */
@Data
@Schema(description = "菜单")
@TableName("wechat_menu")
public class Menu extends BaseEntity {

    @Schema(description = "id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "网页 链接，用户点击菜单可打开链接")
    private String url;

    @Schema(description = "菜单KEY值，用于消息接口推送")
    @TableField("meun_key")
    private String meunKey;

    @Schema(description = "排序")
    private Integer sort;
}
