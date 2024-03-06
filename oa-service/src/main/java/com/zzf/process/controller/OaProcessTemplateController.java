package com.zzf.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.common.result.Result;
import com.zzf.model.process.ProcessTemplate;
import com.zzf.process.service.OaProcessTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Tag(name = "审批模板管理")
@RestController
@RequestMapping(value = "/admin/process/processTemplate")
public class OaProcessTemplateController {

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Operation(summary = "分页查询审批模板数据")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit) {
        Page<ProcessTemplate> pageParam = new Page<>(page,limit);
        //分页查询审批模板，把审批类型对应名称查询
        IPage<ProcessTemplate> pageModel =
                processTemplateService.selectPageProcessTempate(pageParam);
        return Result.ok(pageModel);
    }

    @Operation(summary = "根据id获取审批模板数据")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    @Operation(summary = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.updateById(processTemplate);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTemplateService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws IOException {
        //目标是上传到classes目录，先获取该目录位置
        String path = new File(ResourceUtils.getURL("classpath:")
                .getPath()).getAbsolutePath();
        //设置上传文件夹
        File tempFile = new File(path + "/processes/");
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        //创建空文件，实现文件写入
        String filename = file.getOriginalFilename();
        File zipFile = new File(path + "/processes/" + filename);
        file.transferTo(zipFile);
        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));
        return Result.ok(map);
    }
    @Operation(summary = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {
        //修改模板发布状态 1 已经发布
        //流程定义部署
        processTemplateService.publish(id);
        return Result.ok();
    }

}
