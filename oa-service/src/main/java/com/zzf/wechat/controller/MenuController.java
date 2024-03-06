package com.zzf.wechat.controller;

import com.zzf.common.result.Result;
import com.zzf.model.vo.wechat.MenuVo;
import com.zzf.wechat.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zzf
 * @date 2024-02-08
 */
@Tag(name = "微信公众号菜单管理")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Operation(summary = "获取全部菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> menuList = menuService.findMenuInfo();
        return Result.ok(menuList);
    }

    @Operation(summary = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu() {
        menuService.syncMenu();
        return Result.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu() {
        menuService.removeMenu();
        return Result.ok();
    }
}
