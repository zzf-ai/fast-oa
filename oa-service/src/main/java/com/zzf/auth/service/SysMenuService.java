package com.zzf.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.system.SysMenu;
import com.zzf.model.vo.system.AssignMenuVo;
import com.zzf.model.vo.system.MenuAndPermsVo;
import com.zzf.model.vo.system.RouterVo;

import java.util.List;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-26
 */
public interface SysMenuService extends IService<SysMenu> {

    //菜单列表接口
    List<SysMenu> findNodes();

    //删除菜单
    void removeMenuById(Long id);

    //查询所有菜单和角色分配的菜单
    List<SysMenu> findMenuByRoleId(Long roleId);

    //角色分配菜单
    void doAssign(AssignMenuVo assignMenuVo);


//    //根据用户id获取用户可以操作菜单列表
//    List<RouterVo> findUserMenuListByUserId(Long userId);
//
//    //根据用户id获取用户可以操作按钮列表
//    List<String> findUserPermsByUserId(Long userId);

    //根据用户id获取用户可以操作菜单和按钮列表
    MenuAndPermsVo findUserMenuAndPermsListByUserId(Long userId);
}
