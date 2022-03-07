package com.monitordevice.server;

import com.monitordevice.client.MessageType;
import com.monitordevice.configurations.Config;
import com.monitordevice.data.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class is responsible for monitoring messages sent by different devices
 * @author Niharika Sweta
 */
@Slf4j
@Getter
public class MonitorDevices extends Server {
    private static MonitorDevices instance = null;
    private MonitorDevices() throws IOException {
        super(Config.PORT);
        messageCounter = 0;
        connectedDevice = new HashMap<>();
    }

    // To ensure that only one instance of monitor runs
    public static MonitorDevices getInstance() throws IOException {
        if (instance == null) instance = new MonitorDevices();
        return instance;
    }

    @Override
    public synchronized Message<?> handleRequest(Message<?> request) {
        log.info("Received message {} from device ID {}  ", request.getData(), request.getDeviceId());
        messageCounter++;
        long deviceId = request.getDeviceId();
        int messageCount = connectedDevice.getOrDefault(deviceId, 0);
        connectedDevice.put(deviceId, messageCount+1);
        log.info("id {} data {}" , deviceId, messageCount);
        deviceSpecificMessageCount = connectedDevice.get(request.getDeviceId());
        connectedDevice.put(request.getDeviceId(), deviceSpecificMessageCount+1);
        log.info("Client {} has send {} messages", request.getDeviceId(), connectedDevice.get(request.getDeviceId()));
        log.info("Total messages received from all the devices {}", messageCounter);
        return new Message<>(request.getDeviceId(), MessageType.SEND_DATA, "Acknowledgment for " + request.getDeviceId());
    }
}
