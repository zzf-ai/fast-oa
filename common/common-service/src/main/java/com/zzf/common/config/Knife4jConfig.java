package com.zzf.common.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzf
 * @date 2024-01-25
 */
@Configuration
public class Knife4jConfig {

    //用户api
    @Bean
    public GroupedOpenApi userApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("user-api")         // 分组名称
                .pathsToMatch("/api/**")  // 接口请求路径规则
                .build();
    }

    //管理员api
    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("admin-api")         // 分组名称
                .pathsToMatch("/admin/**")  // 接口请求路径规则
                .build();
    }

    //自定义接口文档
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("oa接口文档")
                        // 接口文档描述
                        .description("Knife4j OpenApi 3 example application")
                        // 接口文档版本
                        .version("v1.0")
                        // 开发者联系方式
                        .contact(new Contact().name("zzf")));
    }


}
