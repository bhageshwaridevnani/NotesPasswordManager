package com.example.demo.Config;

import com.example.demo.Util.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {

        ParameterBuilder aParameterBuilders = new ParameterBuilder();
        List<Parameter> aParameters = new ArrayList<>();

        aParameterBuilders.name("userId").modelRef(new ModelRef("string"))
                .description("Description - this is for userId")
                .parameterType("header")
                .required(false)
                .build();
        aParameters.add(aParameterBuilders.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo")) // Replace with your base package
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .globalOperationParameters(aParameters); // Add this line to enable the API key authentication
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Notes Password Manager APIs")
                .description("This is the APIs of notes and password manager to secure your notes and passwords")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(Constant.API_KEY, Constant.ACCESS_TOKEN, "header");
    }
}
