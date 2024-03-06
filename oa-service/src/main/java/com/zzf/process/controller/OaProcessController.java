package com.zzf.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.common.result.Result;
import com.zzf.model.vo.process.ProcessQueryVo;
import com.zzf.model.vo.process.ProcessVo;
import com.zzf.process.service.OaProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Tag(name = "管理端审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
public class OaProcessController {

    @Autowired
    private OaProcessService processService;

    @Operation(summary = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageParam = new Page<>(page,limit);
        IPage<ProcessVo> pageModel =
                processService.selectPage(pageParam,processQueryVo);
        return Result.ok(pageModel);
    }
}
