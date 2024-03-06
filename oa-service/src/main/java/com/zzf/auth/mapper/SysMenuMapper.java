package com.zzf.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzf.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-26
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //多表关联查询：用户角色关系表 、 角色菜单关系表、 菜单表
    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);
}
