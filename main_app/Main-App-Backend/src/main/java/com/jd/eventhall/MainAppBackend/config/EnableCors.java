package com.jd.eventhall.MainAppBackend.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class EnableCors implements WebMvcConfigurer {
    final String path;
    final String[] origins;

    public EnableCors(String path, String[] origins) {
        this.path = path;
        this.origins = origins;
    }

    // When allowCredentials is true, allowedOrigins cannot contain the special value "*"
    //  since that cannot be set on the "Access-Control-Allow-Origin" response header.
    //   To allow credentials to a set of origins, list them explicitly or
    //    consider using "allowedOriginPatterns" instead.

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(path)
            .allowCredentials(true)
            .allowedOrigins(origins)
            // .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT)
            .allowedMethods(HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name());
    }
}
