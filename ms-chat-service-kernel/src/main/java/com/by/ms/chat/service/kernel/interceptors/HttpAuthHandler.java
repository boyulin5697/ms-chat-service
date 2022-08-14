package com.by.ms.chat.service.kernel.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.by.ms.chat.service.kernel.consts.consts.CommonConsts;
import com.by.ms.chat.service.kernel.entity.ChatWebSocket;
import com.by.ms.chat.service.kernel.entity.MessageString;
import com.by.ms.chat.service.kernel.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ,,,
 *
 * @author by.
 * @date 2022/8/14
 */
@Component
@Slf4j
public class HttpAuthHandler extends TextWebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object userNo = session.getAttributes().get("userNo");
        if(userNo!=null){
            ChatWebSocket.add((String) userNo,session);
            String oqName = userNo+CommonConsts.OFFLINE_QUEUE;
            if(RedisUtil.hasKey(oqName)){
                long messageSize = RedisUtil.lGetListSize(oqName);
                for(int i = 0;i<messageSize-1;i++){
                    MessageString ms = (MessageString) RedisUtil.lPop(oqName);
                    TextMessage textMessage = new TextMessage(ms.getMessageContent());
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {}
                }
            }
        }else {
            session.close();
            throw new RuntimeException("The connect of the user is invaild!");
        }
    }

    /**
     * Accept the message from one client and deliver to another
     * Temperately, we don't consider time of sending.
     * @param session
     * @param message
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        String sender = (String) session.getAttributes().get("userNo");
        MessageString ms = JSONObject.parseObject(payload,MessageString.class);
        Assert.hasText(ms.getDestId(),"The destination cannot be null!");
        log.info("A message was delivered from {} to {}",sender,ms.getDestId());
        if(ChatWebSocket.get(ms.getDestId())!=null){
            WebSocketSession destSession = ChatWebSocket.get(ms.getDestId());
            destSession.sendMessage(message);
        }else{
            String queueName = ms.getDestId()+CommonConsts.OFFLINE_QUEUE;
            if(RedisUtil.hasKey(queueName)){
                RedisUtil.set(queueName,"-a");
            }
            RedisUtil.lPush(queueName,ms, TimeUnit.DAYS.toMillis(7));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException{
        Object userNo = session.getAttributes().get("userNo");
        if(userNo!=null){
            ChatWebSocket.remove((String)userNo);
        }
    }
}
