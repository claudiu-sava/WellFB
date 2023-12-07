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
        registry.addResourceHandler("/uploads/**", "/drawable/**", "/css/**", "/js/**", "/avatars/**", "/hq/**")
                .addResourceLocations("file:" + UPLOAD_DIRECTORY, "file:" + DRAWABLE_RESOURCES, "file:" + CSS_RESOURCES, "file:" + JS_RESOURCES, "file:" + AVATAR_DIRECTORY, "file:" + HQ_UPLOAD_DIRECTORY)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}