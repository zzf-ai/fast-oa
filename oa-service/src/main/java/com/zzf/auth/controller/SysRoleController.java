package com.zzf.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.auth.service.SysRoleService;
import com.zzf.common.result.Result;
import com.zzf.model.system.SysRole;
import com.zzf.model.vo.system.AssignRoleVo;
import com.zzf.model.vo.system.SysRoleQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-25
 */
@Tag(name = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    // 查询所有角色 和 当前用户所属角色
    @Operation(summary = "获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    // 为用户分配角色
    @Operation(summary = "为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo) {
        sysRoleService.doAssign(assignRoleVo);
        return Result.ok();
    }


    @Operation(summary = "查询所有角色")
    @GetMapping("/findAll")
    public Result findAll() {
        //调用service的方法
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    //条件分页查询
    //page 当前页  limit 每页显示记录数
    //SysRoleQueryVo 条件对象
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @Operation(summary = "条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //调用service的方法实现
        //1 创建Page对象，传递分页相关参数
        //page 当前页  limit 每页显示记录数
        Page<SysRole> pageParam = new Page<>(page,limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)) {
            //封装 like模糊查询
            wrapper.like(SysRole::getRoleName,roleName);
        }

        //3 调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    //添加角色
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @Operation(summary = "添加角色")
    @PostMapping ("save")
    public Result save(@RequestBody SysRole role) {
        //调用service的方法
        boolean is_success = sysRoleService.save(role);
        if(is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改角色-根据id查询
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @Operation(summary = "根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    //修改角色-最终修改
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @Operation(summary = "修改角色")
    @PutMapping ("update")
    public Result update(@RequestBody SysRole role) {
        //调用service的方法
        boolean is_success = sysRoleService.updateById(role);
        if(is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id删除
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @Operation(summary = "根据id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysRoleService.removeById(id);
        if(is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除
    // 前端数组 [1,2,3]
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @Operation(summary = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysRoleService.removeByIds(idList);
        if(is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
}
