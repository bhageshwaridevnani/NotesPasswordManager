package com.example.demo.Config;


import com.example.demo.Security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public MapperFactory mapperFactory() {
//        return new DefaultMapperFactory.Builder().build();
//    }
//
//    @Bean
//    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
//        return mapperFactory.getMapperFacade();
//    }


//    @Bean
//    public MapperFactory mapperFactory() {
//        return new DefaultMapperFactory.Builder().build();
//    }
//
//    @Bean
//    public MapperFacade mapperFacade() {
//        return mapperFactory().getMapperFacade();
//    }
}
