package com.zzf.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzf.auth.service.SysUserService;
import com.zzf.model.process.Process;
import com.zzf.model.process.ProcessRecord;
import com.zzf.model.process.ProcessTemplate;
import com.zzf.model.system.SysUser;
import com.zzf.model.vo.process.ApprovalVo;
import com.zzf.model.vo.process.ProcessFormVo;
import com.zzf.model.vo.process.ProcessQueryVo;
import com.zzf.model.vo.process.ProcessVo;
import com.zzf.process.mapper.OaProcessMapper;
import com.zzf.process.service.MessageService;
import com.zzf.process.service.OaProcessRecordService;
import com.zzf.process.service.OaProcessService;
import com.zzf.process.service.OaProcessTemplateService;
import com.zzf.security.custom.LoginUserInfoHelper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author zzf
 * @date 2024-01-31
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OaProcessRecordService processRecordService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MessageService messageService;

    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //部署
        repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam,processQueryVo);
        return pageModel;
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        //根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        //根据审批模板id把模板信息查询
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
        //保存提交审批信息到业务表，oa_process
        Process process = new Process();
        //processFormVo复制到process对象里面
        BeanUtils.copyProperties(processFormVo,process);
        //其他值
        process.setStatus(1); //审批中
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);

        //启动流程实例 - RuntimeService
        //流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        //业务key processId
        String businessKey = String.valueOf(process.getId());
        //封装流程参数
        // form表单json数据，转换map集合
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        //遍历formData得到内容，封装map集合
        Map<String,Object> map = new HashMap<>();
        for(Map.Entry<String,Object> entry:formData.entrySet()) {
            map.put(entry.getKey(),entry.getValue());
        }
        //流程参数
        Map<String,Object> variables = new HashMap<>();
        variables.put("data",map);
        //启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
                businessKey, variables);
        //查询下一个审批人(可能多个）
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for(Task task: taskList){
            //审批该任务的用户名
            String assigneeName = task.getAssignee();
            //获取真实姓名
            SysUser user = sysUserService.getUserByUserName(assigneeName);
            String name = user.getName();
            nameList.add(name);
            //推送消息
            messageService.pushPendingMessage(process.getId(), sysUser.getId(), task.getId());
        }
        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待"+ StringUtils.join(nameList.toArray(), ",")+"审批");
        //业务和流程关联 更新oa_process数据
        baseMapper.updateById(process);

        //记录操作审批信息
        processRecordService.record(process.getId(),1,"发起申请");
    }

    //查询待处理任务列表
    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageParam) {
        //封装查询条件，根据当前登录的用户名称查询，按创建时间的降序排列
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        //调用方法listPage分页条件查询，返回list集合，待办任务集合
        //listPage方法有两个参数
        //第一个参数：开始位置  第二个参数：每页显示记录数
        int begin = (int)((pageParam.getCurrent()-1)*pageParam.getSize());
        int size = (int)pageParam.getSize();
        List<Task> taskList = query.listPage(begin, size);
        long totalCount = query.count();
        // 封装返回list集合数据 到 List<ProcessVo>里面
        //List<Task> -- List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for(Task task : taskList){
            //从task获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            //根据流程实例id获取实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            //从流程实例对象获取业务key---processId
            String businessKey = processInstance.getBusinessKey();
            if(businessKey == null) {
                continue;
            }
            //根据业务key获取Process对象
            long processId = Long.parseLong(businessKey);
            Process process = baseMapper.selectById(processId);
            //Process对象 复制 ProcessVo对象
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(task.getId());
            //放到最终list集合processVoList
            processVoList.add(processVo);
        }
        // 封装返回IPage对象
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(),
                pageParam.getSize(), totalCount);
        page.setRecords(processVoList);
        return page;
    }

    //查看审批详情信息
    @Override
    public Map<String, Object> show(Long id) {
        //根据流程id获取流程信息
        Process process = baseMapper.selectById(id);
        //根据流程id获取流程记录信息
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId,id);
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        //根据模板id查询模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        //判断当前用户是否可以审批
        boolean isApprove = false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for(Task task : taskList) {
            //判断任务审批人是否是当前用户
            String username = LoginUserInfoHelper.getUsername();
            if(task.getAssignee().equals(username)) {
                isApprove = true;
            }
        }
        // 查询数据封装到map集合，返回
        Map<String,Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    //审批
    @Override
    public void approve(ApprovalVo approvalVo) {
        //从approve获取任务id，根据任务id和获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);

        //判断审批状态值
        if(approvalVo.getStatus() == 1){
            //状态值=1表示审批通过
            Map<String, Object> variable = new HashMap<>();
            taskService.complete(taskId,variable);
        }else{
            //状态值=-1表示审批驳回，流程直接结束
            this.endTask(taskId);
        }
        //记录审批信息
        String description = approvalVo.getStatus().intValue() ==1 ? "已通过" : "驳回";
        processRecordService.record(approvalVo.getProcessId(),
                approvalVo.getStatus(),description);
        // 查询下一个审批人，更新流程表记录 process表记录
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        //查询任务
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(taskList)){
            List<String> assignList = new ArrayList<>();
            for(Task task : taskList) {
                String assignee = task.getAssignee();
                SysUser sysUser = sysUserService.getUserByUserName(assignee);
                assignList.add(sysUser.getName());

                // 公众号消息推送
                messageService.pushProcessedMessage(process.getId(), process.getUserId(), approvalVo.getStatus());
            }
            //更新process流程信息
            process.setDescription("等待" + StringUtils.join(assignList.toArray(), ",") + "审批");
            process.setStatus(1);
        }else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（通过）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（驳回）");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
    }

    //查询已处理任务列表
    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskAssignee(LoginUserInfoHelper.getUsername())
                .finished().orderByTaskCreateTime().desc();
        //调用方法条件分页查询，返回List集合
        //开始位置和每页显示记录数
        int begin = (int)((pageParam.getCurrent()-1)*pageParam.getSize());
        int size = (int)pageParam.getSize();
        List<HistoricTaskInstance> list = query.listPage(begin, size);
        long totalCount = query.count();

        //遍历返回list集合，封装List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for(HistoricTaskInstance item : list){
            //流程实例id
            String processInstanceId = item.getProcessInstanceId();
            //根据流程实例id查询获取process信息
            LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId,processInstanceId);
            Process process = baseMapper.selectOne(wrapper);
            // process -- processVo
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId("0");
            //放到list
            processVoList.add(processVo);
        }
        //IPage封装分页查询所有数据，返回
        IPage<ProcessVo> pageModel = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        for (ProcessVo item : pageModel.getRecords()) {
            item.setTaskId("0");
        }
        return pageModel;
    }

    //结束流程
    private void endTask(String taskId){
        // 根据任务id获取任务对象 Task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //获取流程定义模型BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //获取结束流向节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);

        if(CollectionUtils.isEmpty(endEventList)){
            return;
        }
        FlowNode endFlowNode = endEventList.get(0);
        //当前流向节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        //临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        // 清理当前流动方向
        currentFlowNode.getOutgoingFlows().clear();

        // 创建新流向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        // 当前节点指向新方向
        List newSequenceFlowList = new ArrayList();
        newSequenceFlowList.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        // 完成当前任务
        taskService.complete(task.getId());
    }
    //当前任务列表
    private List<Task> getCurrentTaskList(String id) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(id).list();
        return taskList;
    }
}
