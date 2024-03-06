package com.zzf.model.vo.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author zzf
 * @date 2024-01-29
 */
@Data
@AllArgsConstructor
public class MenuAndPermsVo {

    List<RouterVo> routerList;

    List<String> permsList;
}
