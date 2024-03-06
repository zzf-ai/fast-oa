package com.zzf.model.vo.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-02-13
 */
@Data
public class BindPhoneVo {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "openId")
    private String openId;
}
