package com.zzf.wechat.service;

import com.zzf.model.vo.wechat.MenuVo;

import java.util.List;

/**
 * @author zzf
 * @date 2024-02-08
 */
public interface MenuService {
    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
