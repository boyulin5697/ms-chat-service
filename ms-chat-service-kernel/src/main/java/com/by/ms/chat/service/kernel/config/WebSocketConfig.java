package com.by.ms.chat.service.kernel.config;

import com.by.ms.chat.service.kernel.interceptors.GeneralInterceptor;
import com.by.ms.chat.service.kernel.interceptors.HttpAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * web socket config
 *
 * @author by.
 * @date 2022/8/14
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private HttpAuthHandler httpAuthHandler;

    @Autowired
    private GeneralInterceptor generalInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(httpAuthHandler,"/chatWs")
                .addInterceptors(generalInterceptor)
                .setAllowedOriginPatterns("*");

    }
}
