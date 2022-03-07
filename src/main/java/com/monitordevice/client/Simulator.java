package com.monitordevice.client;

import com.monitordevice.configurations.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class simulates the multiple device environment.
 * Assumption: NUMBER_OF_DEVICES ->5
 * @author Niharika Sweta
 */
@Slf4j
public class Simulator {
    private static final int NUMBER_OF_DEVICES = 5;

    public static void main(String[] args) {
        ExecutorService clientProcessingPool = Executors.newFixedThreadPool(Config.THREAD_COUNT);
        for (int i = 0; i < NUMBER_OF_DEVICES; i++) {
            log.info("Client {} created", i);
            clientProcessingPool.execute(new MessageSendingDevice(UUID.randomUUID().getMostSignificantBits()
                    & Long.MAX_VALUE));
        }
            // Shutdown the executor pool
            clientProcessingPool.shutdown();
        }
}
