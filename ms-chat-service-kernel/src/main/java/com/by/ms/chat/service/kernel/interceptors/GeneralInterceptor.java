package com.by.ms.chat.service.kernel.interceptors;

import com.cyperspace.b.user.apis.UserServiceApis;
import com.cyperspace.b.user.service.api.requests.QueryUserOnlineRequest;
import com.cyperspace.b.user.service.api.responses.QueryUserOnlineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * General websocket interceptor
 *
 * @author by.
 * @date 2022/8/14
 */

@Component
@Slf4j
public class GeneralInterceptor implements HandshakeInterceptor {

    @Autowired
    private UserServiceApis userServiceApis;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String requestUserNo = request.getHeaders().get("Auth").get(0);
        if(requestUserNo==null||requestUserNo.length()<=0){
            log.warn("The request is illegal!");
            return false;
        }
        QueryUserOnlineRequest onlineRequest = new QueryUserOnlineRequest();
        onlineRequest.setDestUserId(requestUserNo);
        QueryUserOnlineResponse resp = userServiceApis.queryUserOnline(onlineRequest);
        if(resp.getCode()!=200){
            log.warn("Query user service occurs error{}! The user no is{}",resp.getMessage(),requestUserNo);
            return false;
        }else{
            if(resp.getOnline()){
                log.info("HandShake with user client{} success!",requestUserNo);
                attributes.put("userNo",requestUserNo);
                return true;
            }
            log.warn("An offline user:{} tried to login in!",requestUserNo);
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("Hand Shake succeed!");
    }
}
