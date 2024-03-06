package com.zzf.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.model.system.SysUser;

import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-26
 */
public interface SysUserService extends IService<SysUser> {
    void updateStatus(Long id, Integer status);

    SysUser getUserByUserName(String username);

    Map<String, Object> getCurrentUser();
}

