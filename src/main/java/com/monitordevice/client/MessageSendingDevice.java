package com.monitordevice.client;

import com.monitordevice.configurations.Config;
import com.monitordevice.data.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Device class which send data to the MonitorServer
 * @author Niharika Sweta
 */
@Slf4j
@Getter
public class MessageSendingDevice implements Runnable {
    private final long deviceId;

    public MessageSendingDevice(long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Sends the message to the MonitoringDevice server.
     * To simulate the environment , using do while lop and 10 messages are being sent.
     * To stop sending data 11th message is of MessageType.STOP_SEND. This is done in ordr to close the client.
     */

    public void run() {
        log.info("Sending message from device id {}", deviceId);
        int i = 0;
        try (Socket socket = new Socket(Config.SERVER_HOST, Config.PORT)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            do {
                i++;
                Message<?> idRequest = new Message<>(deviceId, MessageType.SEND_DATA, "Hi Server " + deviceId);
                oos.writeObject(idRequest);
                Object response = ois.readObject();
                log.info("Client {} received response  from server", ((Message<?>) response).getDeviceId());

                Thread.sleep(1000);
            } while (i < 10);

            Message<?> idRequest = new Message<>(deviceId, MessageType.STOP_SEND, "Bye Server" + deviceId);
            oos.writeObject(idRequest);
            Object response = ois.readObject();
            /*log.info("Client received last response {}  and message {} \"", ((Message<?>) response).getDeviceId(),
                    ((Message<?>) response).getData());*/

            oos.reset();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            log.error("Interrupted!", e);
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
