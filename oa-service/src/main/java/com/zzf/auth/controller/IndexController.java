package com.zzf.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzf.auth.service.SysMenuService;
import com.zzf.auth.service.SysUserService;
import com.zzf.common.exception.CustomException;
import com.zzf.common.jwt.JwtHelper;
import com.zzf.common.result.Result;
import com.zzf.model.system.SysUser;
import com.zzf.model.vo.system.LoginVo;
import com.zzf.model.vo.system.MenuAndPermsVo;
import com.zzf.model.vo.system.RouterVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-29
 */
@Tag(name = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        //1 获取输入用户名和密码
        //2 根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        //3 用户信息是否存在
        if(sysUser == null) {
            throw new CustomException(201,"用户不存在");
        }

        //4 判断密码
        //数据库存密码（MD5）
        String password_db = sysUser.getPassword();
        //获取输入的密码
        String password_input = DigestUtils.md5DigestAsHex(loginVo.getPassword().getBytes());
        if(!password_db.equals(password_input)) {
            throw new CustomException(201,"密码错误");
        }

        //5 判断用户是否被禁用  1 可用 0 禁用
        if(sysUser.getStatus().intValue()==0) {
            throw new CustomException(201,"用户已经被禁用");
        }

        //6 使用jwt根据用户id和用户名称生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        //7 返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }
    @Operation(summary = "获取登录用户的信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //1 从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");

        //2 从token字符串获取用户id 或者 用户名称
        Long userId = JwtHelper.getUserId(token);

        //3 根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);

        //4 根据用户id获取用户可以操作菜单列表
        //查询数据库动态构建路由结构，进行显示
        MenuAndPermsVo menuAndPermsVo = sysMenuService.findUserMenuAndPermsListByUserId(userId);

        List<RouterVo> routerList = menuAndPermsVo.getRouterList();

        //5 根据用户id获取用户可以操作按钮列表
        List<String> permsList = menuAndPermsVo.getPermsList();

        //6 返回相应的数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作菜单
        map.put("routers",routerList);
        //返回用户可以操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
