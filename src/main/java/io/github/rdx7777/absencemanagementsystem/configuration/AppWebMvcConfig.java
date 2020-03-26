package io.github.rdx7777.absencemanagementsystem.configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class AppWebMvcConfig {

//    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // forward requests to /admin and /user to their index.html
//                registry.addViewController("/admin").setViewName(
//                    "forward:/admin/index.html");
//                registry.addViewController("/user").setViewName(
//                    "forward:/user/index.html");
                registry.addViewController("/").setViewName(
                    "forward:/index.html");
            }
        };
    }
}
