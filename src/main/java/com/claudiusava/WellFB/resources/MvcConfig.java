package com.claudiusava.WellFB.resources;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import static com.claudiusava.WellFB.WellFbApplication.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**", "/drawable/**", "/css/**", "/js/**")
                .addResourceLocations("file:" + UPLOAD_DIRECTORY, "file:" + DRAWABLE_RESOURCES, "file:" + CSS_RESOURCES, "file:" + JS_RESOURCES)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}