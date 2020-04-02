package com.eduardo.mancala.config;

import com.fasterxml.classmate.TypeResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfiguration {

    @NonNull
    private TypeResolver resolver;

    @Bean
    public Docket diffApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eduardo.mancala.controller"))
                .build()
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(getModelClasses())
                .apiInfo(new ApiInfoBuilder().title("Mancala API").build())
                .tags(
                        new Tag("boards", "Services related with board")
                );
    }

    private Class[] getModelClasses() {
        Reflections reflections = new Reflections("com.eduardo.mancala.model.entity");
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        return allClasses.toArray(Class[]::new);
    }
}
