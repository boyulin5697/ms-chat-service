package com.by.ms.chat.service.kernel.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Message String, sending and
 *
 * @author by.
 * @date 2022/8/14
 */
@Data
public class MessageString implements Serializable {

    private String destId;

    private String messageContent;
}
