package com.zzf.process.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.auth.service.SysUserService;
import com.zzf.common.result.Result;
import com.zzf.model.process.Process;
import com.zzf.model.process.ProcessTemplate;
import com.zzf.model.process.ProcessType;
import com.zzf.model.vo.process.ApprovalVo;
import com.zzf.model.vo.process.ProcessFormVo;
import com.zzf.model.vo.process.ProcessVo;
import com.zzf.process.service.OaProcessService;
import com.zzf.process.service.OaProcessTemplateService;
import com.zzf.process.service.OaProcessTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-02-02
 */
@Tag(name = "员工端审批流管理")
@RestController
@RequestMapping(value="/admin/process")
@CrossOrigin //跨域
public class ProcessController {
    @Autowired
    private OaProcessTypeService processTypeService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private OaProcessService processService;

    @Autowired
    private SysUserService sysUserService;


    @Operation(summary = "查询所有审批分类和每个分类所有审批模板")
    @GetMapping("findProcessType")
    public Result findProcessType() {
        List<ProcessType> list = processTypeService.findProcessType();
        return Result.ok(list);
    }

    @Operation(summary = "获取审批模板数据")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @Operation(summary = "启动流程")
    @PostMapping("/startUp")
    public Result startUp(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.ok();
    }

    @Operation(summary = "查询待处理任务")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page,limit);
        IPage<ProcessVo> pageModel = processService.findPending(pageParam);
        return Result.ok(pageModel);
    }

    @Operation(summary = "查看审批详情信息")
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        Map<String,Object> map = processService.show(id);
        return Result.ok(map);
    }

    @Operation(summary = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }

    @Operation(summary = "查询已处理任务")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page,limit);
        IPage<ProcessVo> pageModel = processService.findProcessed(pageParam);
        return Result.ok(pageModel);
    }

    @Operation(summary = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page, @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findStarted(pageParam);
        return Result.ok(pageModel);
    }

    @Operation(summary = "获取当前用户的信息")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
        Map<String,Object> map = sysUserService.getCurrentUser();
        return Result.ok(map);
    }
}
