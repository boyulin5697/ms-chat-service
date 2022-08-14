package com.by.ms.chat.service.kernel.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.ConcurrentHashMap;


/**
 * chat web socket
 *
 * @author by.
 * @date 2022/8/14
 */
@Component
@Slf4j
public class ChatWebSocket {

    /**
     * Session Pool
     */
    private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();


    /**
     * add session
     * @param key
     * @param session
     */
    public static void add(String key, WebSocketSession session){
        SESSION_POOL.put(key,session);
    }

    /**
     * remove session
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key){
        return SESSION_POOL.remove(key);
    }

    /**
     * remove, and close session
     * @param key
     */
    public static void removeAndCloseSession(String key){
        WebSocketSession session = remove(key);
        try{
            session.close();
        }catch (Exception e){
            log.error("Failed closing session {}",key);
        }
    }


    /**
     * get session
     * @param key
     * @return
     */
    public static WebSocketSession get(String key){
        return SESSION_POOL.get(key);
    }

}
