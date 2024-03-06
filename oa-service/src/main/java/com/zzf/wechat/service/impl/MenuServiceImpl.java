package com.zzf.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.model.vo.wechat.MenuVo;
import com.zzf.model.wechat.Menu;
import com.zzf.wechat.mapper.MenuMapper;
import com.zzf.wechat.service.MenuService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzf
 * @date 2024-02-08
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Autowired
    private WxMpService wxMpService;

    //获取全部菜单
    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo> list = new ArrayList<>();

        //查询所有菜单list集合
        List<Menu> menuList = baseMapper.selectList(null);
        //一级菜单的list集合
        List<Menu> oneMenuList = menuList.stream()
                .filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());
        for(Menu oneMenu : oneMenuList){
            //一级菜单Menu --- MenuVo
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,oneMenuVo);
            // 获取每个一级菜单里面所有二级菜单 id 和 parent_id比较
            //一级菜单id  和  其他菜单parent_id
            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                    .collect(Collectors.toList());
            // 把一级菜单里面所有二级菜单获取到，封装一级菜单children集合里面
            //List<Menu> -- List<MenuVo>
            List<MenuVo> children = new ArrayList<>();
            for(Menu twoMenu : twoMenuList) {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoMenuVo);
                children.add(twoMenuVo);
            }
            oneMenuVo.setChildren(children);
            //把每个封装好的一级菜单放到最终list集合
            list.add(oneMenuVo);
        }
        return list;
    }

    @Override
    public void syncMenu() {
        // 菜单数据查询出来，封装微信要求菜单格式
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://ggkt1.vipgz1.91tunnel.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://ggkt1.vipgz1.91tunnel.com#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        // 调用工具里面的方法实现菜单推送
        try {
            wxMpService.getMenuService().menuCreate(button.toString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
