package com.zzf.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzf.auth.service.SysUserService;
import com.zzf.model.process.Process;
import com.zzf.model.process.ProcessTemplate;
import com.zzf.model.system.SysUser;
import com.zzf.process.service.MessageService;
import com.zzf.process.service.OaProcessService;
import com.zzf.process.service.OaProcessTemplateService;
import com.zzf.security.custom.LoginUserInfoHelper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zzf
 * @date 2024-02-13
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private OaProcessService processService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.pendMsgId}")
    private String pendMsgId;

    @Value("${wechat.processMsgId}")
    private String processMsgId;

    //推送待审批人员
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        //查询流程信息
        Process process = processService.getById(processId);
        //查询要推送的人员信息
        SysUser sysUser = sysUserService.getById(userId);
        //查询审批模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        //获取提交审批人的信息
        SysUser submitSysUser = sysUserService.getById(process.getUserId());

        //获取要推送消息的人员的openId
        String openId = sysUser.getOpenId();
        if(StringUtils.isEmpty(openId)){
            openId = "oVeEt60tyB5HsyfnTqkNIFSNET24";
        }
        //设置消息发送信息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                //推送的人的openId
                .toUser(openId)
                //模板消息Id
                .templateId(pendMsgId)
                //点击消息，跳转到审批详情
                .url("http://ggkt1.vipgz1.91tunnel.com/#/show/" + processId + "/" + taskId)
                .build();
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }
        //设置模板里面参数值
        templateMessage
                .addData(new WxMpTemplateData("first",
                        submitSysUser.getName()+"提交"+processTemplate.getName()+",请注意查看","#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        //调用方法发送
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    //推送审批完成的消息
    @Override
    public void pushProcessedMessage(Long processId, Long userId, Integer status) {
        Process process = processService.getById(processId);
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        SysUser sysUser = sysUserService.getById(userId);
        SysUser currentSysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        String openid = sysUser.getOpenId();
        if(StringUtils.isEmpty(openid)) {
            openid = "oVeEt60tyB5HsyfnTqkNIFSNET24";
        }
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId(processMsgId)//模板id
                .url("http://oa.atguigu.cn/#/show/"+processId+"/0")//点击模板消息要访问的网址
                .build();
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }
        templateMessage.addData(new WxMpTemplateData("first", "你发起的"+processTemplate.getName()+"审批申请已经被处理了，请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", currentSysUser.getName(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", status == 1 ? "审批通过" : "审批拒绝", status == 1 ? "#009966" : "#FF0033"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
