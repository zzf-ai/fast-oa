package com.zzf.test;

import com.zzf.model.process.ProcessType;
import com.zzf.process.mapper.OaProcessTemplateMapper;
import com.zzf.process.mapper.OaProcessTypeMapper;
import com.zzf.process.service.OaProcessTemplateService;
import com.zzf.process.service.OaProcessTypeService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author zzf
 * @date 2024-02-02
 */
@SpringBootTest
@MapperScan(value = "com.zzf.process.mapper")
public class ProcessTest {

    @Autowired
    private OaProcessTypeService processTypeService;

    @Autowired
    private OaProcessTypeMapper processTypeMapper;

    @Test
    public void get() {
        List<ProcessType> list = processTypeService.findProcessType();
        System.out.println(list);
    }

    @Test
    public void get2() {
        List<ProcessType> list = processTypeMapper.selectProcessTypeAndTemplatesList();
        System.out.println(list);
    }


}
