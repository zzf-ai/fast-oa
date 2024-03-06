package com.zzf.auth.controller;

import com.zzf.auth.service.SysMenuService;
import com.zzf.common.result.Result;
import com.zzf.model.system.SysMenu;
import com.zzf.model.vo.system.AssignMenuVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-27
 */
@Tag(name = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @Operation(summary = "查询所有菜单和角色分配的菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @Operation(summary = "角色分配菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.doAssign(assignMenuVo);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @Operation(summary = "菜单列表")
    @GetMapping("findNodes")
    public Result findNodes() {

        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @Operation(summary = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @Operation(summary = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @Operation(summary = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
}
