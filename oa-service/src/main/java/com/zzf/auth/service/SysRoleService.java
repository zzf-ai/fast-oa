package com.zzf.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.system.SysRole;
import com.zzf.model.vo.system.AssignRoleVo;

import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-25
 */
public interface SysRoleService extends IService<SysRole> {

    // 查询所有角色 和 当前用户所属角色
    Map<String, Object> findRoleDataByUserId(Long userId);

    // 为用户分配角色
    void doAssign(AssignRoleVo assignRoleVo);
}
