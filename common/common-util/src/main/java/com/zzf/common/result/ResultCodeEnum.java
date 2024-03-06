package com.zzf.common.result;

import lombok.Getter;

/**
 * @author zzf
 * @date 2024-01-25
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    LOGIN_ERROR(208,"认证失败"),
    PERMISSION(205, "没有操作权限"),
    MENU_ERROR(601,"菜单不能删除"),
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }
}
