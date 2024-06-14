package com.azat4dev.booking.listingsms.queries.application.config.presentation;

import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.*;
import com.azat4dev.booking.shared.domain.values.BaseUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class ApiConfig  implements WebMvcConfigurer  {

    @Bean
    GetListingPhotoUrl getListingPhotoUrl(
        @Qualifier("listingsPhotoBaseUrl")
        BaseUrl baseUrl
    ) {
        return new GetListingPhotoUrlImpl(baseUrl);
    }

    @Bean
    MapListingPhotoToDTO mapListingPhotoToDTO(GetListingPhotoUrl getListingPhotoUrl) {
        return new MapListingPhotoToDTOImpl(getListingPhotoUrl);
    }

    @Bean
    MapListingPrivateDetailsToDTO mapListingPrivateDetailsToDTO(
        MapListingPhotoToDTO mapListingPhoto
    ) {
        return new MapListingPrivateDetailsToDTOImpl(mapListingPhoto);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
//
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//                logger.info("Incoming request data: URL = {}, Method = {}, Headers = {}", request.getRequestURL(), request.getMethod(), request.getHeaderNames());
//                if (!request.getMethod().toUpperCase().equals("PATCH")) {
//                    return true;
//                }
//
//                try {
//                    String body = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//                    logger.info("Incoming request body: {}", body);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                return true;
//            }
//
//            @Override
//            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//                // you can add post handle code here
//            }
//
//            @Override
//            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//                // you can add after completion code here
//            }
//        });
//    }
}
