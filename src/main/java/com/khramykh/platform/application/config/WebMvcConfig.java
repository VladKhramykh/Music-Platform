package com.khramykh.platform.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableSpringDataWebSupport
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH");
    }

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("http://localhost:8080");
            }
        };
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/albums/**")
                .addResourceLocations("file:///" + uploadPath + "/images/albums/");
        registry.addResourceHandler("/img/tracks/**")
                .addResourceLocations("file:///" + uploadPath + "/images/tracks/");
        registry.addResourceHandler("/img/users/**")
                .addResourceLocations("file:///" + uploadPath + "/images/users/");
        registry.addResourceHandler("/img/artists/**")
                .addResourceLocations("file:///" + uploadPath + "/images/artists/");
        registry.addResourceHandler("/tracks/**")
                .addResourceLocations("file:///" + uploadPath + "/tracks/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
//    }
}
