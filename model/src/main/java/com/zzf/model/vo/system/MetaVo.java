package com.zzf.model.vo.system;

/**
 * @author zzf
 * @date 2024-01-29
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由显示信息
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaVo
{
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

}
