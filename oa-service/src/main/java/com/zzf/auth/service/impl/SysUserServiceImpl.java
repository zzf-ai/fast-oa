package com.zzf.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.auth.mapper.SysUserMapper;
import com.zzf.auth.service.SysUserService;
import com.zzf.model.system.SysUser;
import com.zzf.security.custom.LoginUserInfoHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public void updateStatus(Long id, Integer status) {
        //根据userid查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态值
        sysUser.setStatus(status);
        //调用方法进行修改
        baseMapper.updateById(sysUser);
    }

    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        return map;
    }
}
