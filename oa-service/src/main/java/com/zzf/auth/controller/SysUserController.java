package com.zzf.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.auth.service.SysUserService;
import com.zzf.common.result.Result;
import com.zzf.model.system.SysUser;
import com.zzf.model.vo.system.SysUserQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-26
 */
@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户条件分页查询
     *
     * @param page
     * @param pageSize
     * @param sysUserQueryVo
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @Operation(summary = "用户条件分页查询")
    @GetMapping("/{page}/{pageSize}")
    public Result page(@PathVariable int page, @PathVariable int pageSize, SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> sysUserPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 获取条件
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        // 判断条件值不为空
        if (!StringUtils.isEmpty(userName)){
            lambdaQueryWrapper.like(SysUser::getUsername,userName);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            lambdaQueryWrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            lambdaQueryWrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        sysUserService.page(sysUserPage,lambdaQueryWrapper);

        return Result.ok(sysUserPage);
    }

    @Operation(summary = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }


    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @Operation(summary = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) {
        //密码进行加密，使用MD5
        String passwordMD5 = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(passwordMD5);
        boolean is_success = sysUserService.save(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @Operation(summary = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @Operation(summary = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @Operation(summary = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysUserService.removeByIds(idList);
        if(is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @Operation(summary = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }

}
