package com.monitordevice.data;

import com.monitordevice.client.MessageType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Message to be sent to the server.
 * @author Niharika Sweta
 */
@Getter
@Setter
@EqualsAndHashCode
public class Message<T> implements Serializable {
    private static final long serialversionUID = 129348938L;
    private long deviceId;
    private MessageType messageType;
    private T data;

    public Message(long deviceId, MessageType messageType, T data) {
        this.deviceId = deviceId;
        this.messageType = messageType;
        this.data = data;
    }
}
