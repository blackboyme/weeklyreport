package com.caac.weeklyreport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



@EnableOpenApi
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.caac.weeklyreport.controller")) // 指定扫描的包路径
                .paths(PathSelectors.any())                              // 或者可以指定哪些路径下的接口需要生成文档
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("周报系统")              // 网页标题
                .description("周报系统")  // 描述
                .version("1.0")                       // 版本号
                .contact(new Contact("HanRenjie", "http://gmoonlight.love", "254832710@qq.com"))
                .build();
    }
}
