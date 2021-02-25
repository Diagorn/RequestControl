package ru.mpei.requests.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration //Configuring shit that cannot be configured in any other way
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    } //For REST queries (currently used for Captcha)

    @Override
    public void addViewControllers(ViewControllerRegistry registry) { //Configuring mapping for Spring Security (maybe more)
        registry.addViewController("/login").setViewName("login"); //Setting the login page
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //File locations (maybe more)
        registry.addResourceHandler("/static/**") //Static files applyment (such as CSS or JS)
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/img/**") //Images
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/user/img/**") //Images for user pages
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/request/img/**") //Images for request pages
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/request-create/organisation/img/**")
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/request/organisation/img/*.png")
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/request/physical/img/*.png")
                .addResourceLocations("file:///" + uploadPath + File.separator + "img" + File.separator);
        registry.addResourceHandler("/request/physical/files/**")
                .addResourceLocations("file:///" + uploadPath + File.separator + "files" + File.separator);
        registry.addResourceHandler("/request/organisation/files/**")
                .addResourceLocations("file:///" + uploadPath + File.separator + "files" + File.separator);
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + uploadPath + File.separator + "files" + File.separator);
    }
}
